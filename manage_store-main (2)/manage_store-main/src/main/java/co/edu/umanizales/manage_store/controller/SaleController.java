package co.edu.umanizales.manage_store.controller;

import co.edu.umanizales.manage_store.controller.dto.ResponseDTO;
import co.edu.umanizales.manage_store.controller.dto.SaleDTO;
import co.edu.umanizales.manage_store.model.Sale;
import co.edu.umanizales.manage_store.model.Seller;
import co.edu.umanizales.manage_store.model.Store;
import co.edu.umanizales.manage_store.service.SaleService;
import co.edu.umanizales.manage_store.service.SellerService;
import co.edu.umanizales.manage_store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "sale")
public class SaleController {
    @Autowired
    private SaleService saleService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private StoreService storeService;

    @GetMapping
    public ResponseEntity<ResponseDTO> getSales(){
        return new ResponseEntity<>(
                new ResponseDTO(200,
                        saleService.getSales(),
                null),
                HttpStatus.OK);
    }

    @GetMapping(path = "/total")
    public ResponseEntity<ResponseDTO> getTotalSales(){
        return new ResponseEntity<>(new ResponseDTO(200, 
        saleService.getTotalSales(), null),HttpStatus.OK);
    }

    @GetMapping(path = "/totalseller/{code}")
    public ResponseEntity<ResponseDTO> getTotalSalesBySeller(
        @PathVariable String code
    ){
        return new ResponseEntity<>(new ResponseDTO(200, 
        "El vendedor tuvo unas ventas totales de:"+saleService.getTotalSalesBySeller(code), null),
        HttpStatus.OK);
    }

    @GetMapping(path = "/totalstore/{code}")
    public ResponseEntity<ResponseDTO> getTotalSalesByStore(
            @PathVariable String code
    ){
        return new ResponseEntity<>(new ResponseDTO(200,
        "La sucursal vendio un total de:"+saleService.getTotalSalesByStore(code), null),
         HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<ResponseDTO> createSale(@RequestBody SaleDTO saleDTO){
        Seller findSeller = sellerService.getSellerById(saleDTO.getSellerId());
        if( findSeller == null){
            return new ResponseEntity<>(new ResponseDTO(409,
                    "El vendedor ingresado no existe",null),
                    HttpStatus.BAD_REQUEST);
        }
        Store findStore = storeService.getStoreById(saleDTO.getStoreId());
        if( findStore == null){
            return new ResponseEntity<>(new ResponseDTO(409,
                    "La tienda ingresada no existe",null),
                    HttpStatus.BAD_REQUEST);
        }
        saleService.addSale(new Sale(findStore,findSeller,
                saleDTO.getQuantity()));
        return new ResponseEntity<>(new ResponseDTO(200,
                "Venta adicionada",null),
                HttpStatus.OK);
    }

    @GetMapping(path = "/bestseller")
    public ResponseEntity<ResponseDTO> getBestSeller(){
        return new ResponseEntity<>(new ResponseDTO(200,
                saleService.getBestSeller(sellerService.getSellers()), null),HttpStatus.OK);
    }

    @GetMapping(path = "/beststore")
    public ResponseEntity<ResponseDTO> getBestStore(){
        return new ResponseEntity<>(new ResponseDTO(200,
                saleService.getBestStore(storeService.getStores()), null),HttpStatus.OK );
    }


    @GetMapping(path = "/averagesalesbystore")
    public ResponseEntity<ResponseDTO> getAverageSalesByStore() {
        if (storeService.getStores().size() == 0) {
            return new ResponseEntity<>(new ResponseDTO(409,
                    "No hay tiendas ingresadas previamente.", null),
                    HttpStatus.BAD_REQUEST);
        } else {


            return new ResponseEntity<>(new ResponseDTO(200,
                    "El promedio de venta de las sucursales es de:"+ saleService.getTotalSales() / (float) storeService.getStores().size(),
                    null), HttpStatus.OK);
        }
    }
    @GetMapping(path = "/averagesalebyseller")
    public ResponseEntity<ResponseDTO> getAverageSalesBySeller() {
        if (sellerService.getSellers().size() == 0) {
            return new ResponseEntity<>(new ResponseDTO(409,
                    "No hay vendedores ingresados previamente", null),
                    HttpStatus.BAD_REQUEST);
        } else {

            return new ResponseEntity<>(new ResponseDTO(200,
                    "El promedio de venta de los vendedores es de:"+saleService.getTotalSales() / (float) sellerService.getSellers().size(),
                    null), HttpStatus.OK);
        }
    }
}
