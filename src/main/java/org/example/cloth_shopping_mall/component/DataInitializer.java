package org.example.cloth_shopping_mall.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cloth_shopping_mall.entity.BrandCategoryLowestPriceEntity;
import org.example.cloth_shopping_mall.entity.ProductsEntity;
import org.example.cloth_shopping_mall.repository.BrandCategoryALowestPriceRepository;
import org.example.cloth_shopping_mall.repository.ProductsRepository;
import org.example.cloth_shopping_mall.service.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final ProductsRepository productsRepository;
    private final CustomerService customerService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("H2 초기 데이터 동기화 시작");

        List<ProductsEntity> initProducts = productsRepository.findAll();

        if (initProducts.isEmpty()) {
            log.warn("DB에 저장된 데이터 없음");
            return;
        }

        for (ProductsEntity product : initProducts) {
            customerService.syncLowestPrice(product);
        }

        log.info("초기 데이터 동기화 완료");
    }
}
