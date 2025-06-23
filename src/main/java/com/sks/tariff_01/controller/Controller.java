//package com.sks.tariff_01.controller;
//
//
//import com.sks.tariff_01.dto.GSTCodesDto;
//import com.sks.tariff_01.entity.GSTCodes;
//import com.sks.tariff_01.exception.custom.GSTNotFound;
//import com.sks.tariff_01.mapper.MapperImpl;
//import com.sks.tariff_01.model.GstCodeDetail;
//import com.sks.tariff_01.service.AtomFeedService;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api")
//public class Controller {
//    private final AtomFeedService atomFeedService;
//private final MapperImpl mapper = new MapperImpl();
//    public Controller(AtomFeedService atomFeedService) {
//        this.atomFeedService = atomFeedService;
//    }
//
//    @GetMapping("/gst-codes/save")
//    public ResponseEntity<Page<GstCodeDetail>> saveGstCodes(@PageableDefault(size = 5) Pageable pageable) throws Exception {
//        Page<GstCodeDetail> saved = atomFeedService.content(pageable);
//        atomFeedService.savedGST(saved.getContent());
//
//        return ResponseEntity.ok(saved);
//    }
//    @GetMapping("/clear-cache")
//    @CacheEvict(cacheNames = "gstCodes", allEntries = true)
//    public ResponseEntity<String> deleteCache(){
//        return ResponseEntity.ok("Cache cleared");
//    }
//
//
//    @GetMapping(value = "/gst-codes/db", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Page<GSTCodes>> getAllCodes(@PageableDefault(size = 5) Pageable pageable) {
//
//
//        Page<GSTCodes> list = atomFeedService.getAllCodes(pageable);
//        if (list.isEmpty()) {
//            throw new GSTNotFound("No data found");
//        } else {
//            return ResponseEntity.ok(list);
//
//        }
//
//
//
//    }
//    @GetMapping("/search")
//    public ResponseEntity<List<GSTCodesDto>> getByDescription(@RequestParam("keyword") String keyword) {
//        List<GSTCodesDto> gstCodesDto = atomFeedService.searchByDescription(keyword);
//
//        return ResponseEntity.ok(gstCodesDto);
//    }
//
//}