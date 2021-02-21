package com.kodluyoruz.bootcampproject.service;

import com.kodluyoruz.bootcampproject.dto.DeptPaymentAtmWsDto;
import com.kodluyoruz.bootcampproject.entity.Cart.CrediCart;
import com.kodluyoruz.bootcampproject.entity.CartExtract;
import com.kodluyoruz.bootcampproject.entity.Client;
import com.kodluyoruz.bootcampproject.exception.ApiRequestException;
import com.kodluyoruz.bootcampproject.repository.CartExtractRepository;
import com.kodluyoruz.bootcampproject.repository.ClientRepository;
import com.kodluyoruz.bootcampproject.repository.CrediCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CrediCartService {

    @Autowired
    CrediCartRepository crediCartRepository;
    @Autowired
    ClientService clientService;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    CartExtractRepository cartExtractRepository;

    private String createRandomCartNumber(){

        char digits[] = {'0','1','2','3','4','5','6','7','8','9'};

        StringBuilder result = new StringBuilder();

        for(int i=0; i<16; i++) {
            result.append(digits[(int)Math.floor(Math.random() * 10)]);
        }
        return result.toString();
    }

    public void saveCrediCart(CrediCart cart){
        crediCartRepository.save(cart);
    }

    public CrediCart createCrediCart(String identityNumber, Double crediCartLimit) {

        Client getClient = clientService.getClientByIdentityNumber(identityNumber);
        CrediCart newCrediCart = new CrediCart();
        getClient.getCrediCarts().add(newCrediCart);

        newCrediCart.setCartNumber(createRandomCartNumber());
        newCrediCart.setCartLimit(crediCartLimit);
        newCrediCart.setClient(getClient);

        clientRepository.save(getClient);

        return newCrediCart;
    }

    public CrediCart getCrediCart(Long cartId){
       CrediCart crediCart = crediCartRepository.findByCartId(cartId);
       if (ObjectUtils.isEmpty(crediCart)){
           throw new ApiRequestException("Bu kart numarasına ait bir kredi kartı bulunamadı", HttpStatus.NOT_FOUND);
       }
       return crediCart;
    }

    public CrediCart getCrediCartByCartNumber(String cartNumber){
        CrediCart crediCart = crediCartRepository.findByCartNumber(cartNumber);
        if (ObjectUtils.isEmpty(crediCart)){
            throw new ApiRequestException("Kart numarasına ait bir kredi kartı bulunamadı");
        }
        return crediCart;
    }

    public String debtInformation(String cartNumber){
        CrediCart crediCart = getCrediCartByCartNumber(cartNumber);

        return "Kredi kartı borcunuz: "+crediCart.getCartDebt();
       // return ResponseEntity.ok(crediCart);

    }

    public List<CrediCart> getCrediCartsByClient(String identityNumber){
        Client client = clientService.getClientByIdentityNumber(identityNumber);
        List<CrediCart> cartList = crediCartRepository.findCrediCartsByClient(client);
        if (CollectionUtils.isEmpty(cartList)){
            throw new ApiRequestException("Kullanıcıya ait hiç bir kredi kartı bulunamadı.");
        }
        return cartList;
    }


    public CrediCart updateCrediCartLimit(String cartNumber, Double limit) {
        CrediCart crediCart = getCrediCartByCartNumber(cartNumber);
        if (limit<=0){
            throw new ApiRequestException("Kart limiti 0 veya 0'dan küçük olamaz.");
        }
        crediCart.setCartLimit(limit);
        return crediCartRepository.save(crediCart);
    }

    public String deleteCrediCart(String cartNumber) {
        CrediCart crediCart = getCrediCartByCartNumber(cartNumber);
        if (crediCart.getCartDebt()>0){
            throw new ApiRequestException("Kredi kartı borcundan dolayı kart silinemiyor");
        }
        crediCartRepository.delete(crediCart);
        return "Kart silme işlemi başarıyla tamamlandı";
    }

    public ResponseEntity<CrediCart> payDebtWithAtm(DeptPaymentAtmWsDto deptPaymentAtmWsDto){

        CrediCart crediCart = getCrediCartByCartNumber(deptPaymentAtmWsDto.getCartNumber());
        crediCart.setCartDebt(crediCart.getCartDebt()-deptPaymentAtmWsDto.getAmount());
        crediCartRepository.save(crediCart);

        return new ResponseEntity<>(crediCart,HttpStatus.OK);
    }

    public List<CartExtract> getExtract(String cartNumber) {

        CrediCart crediCart = getCrediCartByCartNumber(cartNumber);
        List<CartExtract> cartExtracts = cartExtractRepository.findByCrediCart(crediCart);

        if (CollectionUtils.isEmpty(cartExtracts)) {
            throw new ApiRequestException("Kredi kartına ait ekstre bulunamadı.",
                    HttpStatus.NOT_FOUND);
        }
        return cartExtracts;
    }
}
