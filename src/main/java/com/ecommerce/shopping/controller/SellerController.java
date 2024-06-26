package com.ecommerce.shopping.controller;

import com.ecommerce.shopping.responsedto.UserResponse;
import com.ecommerce.shopping.service.SellerService;
import com.ecommerce.shopping.utility.ResponseStructure;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    //------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/sellers/{sellerId}")
    public ResponseEntity<ResponseStructure<UserResponse>> findSeller(@Valid @PathVariable Long sellerId) {
        return sellerService.findSeller(sellerId);
    }

    //------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/sellers")
    public ResponseEntity<ResponseStructure<List<UserResponse>>> findSellers() {
        return sellerService.findSellers();
    }
}
