package com.prithvi.url.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.prithvi.url.model.Url;
import com.prithvi.url.model.UrlDto;
import com.prithvi.url.model.UrlErrorResponseDto;
import com.prithvi.url.model.UrlResponseDto;
import com.prithvi.url.service.UrlService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UrlShorteningController {

    @Autowired
    private UrlService urlService;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @PostMapping("/home")
    public String homePost(
            @RequestParam("originalLink") String url,
            @RequestParam(value = "customLink", required = false) String customLink,
            ModelMap model
    ) {
        String shortLink = "";

        if (!StringUtils.isEmpty(customLink)) {
            customShortLink(url, customLink);
            shortLink = customLink;
            model.put("shortLink", shortLink);
            return "home";
        }

        ResponseEntity<?> responseEntity = generateShortLink(new UrlDto(url, LocalDateTime.now().plusMinutes(5).toString()));
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Object shortLinkResponseDto = (Object) responseEntity.getBody();
            shortLink = ((UrlResponseDto) shortLinkResponseDto).getShortLink();
        }
        //shortLink = "http://localhost:8080/home/"+shortLink;
        //System.out.println(shortLink);
        model.put("shortLink", shortLink);
        return "home";
    }
    
    //This method is called when the custom link is inserted
    public void customShortLink(String originalLink, String customShortLink) {
        Url url = new Url();
        url.setOriginalUrl(originalLink);
        url.setShortLink(customShortLink);
        url.setCreationDate(LocalDateTime.now());
        url.setExpirationDate(LocalDateTime.now().plusMinutes(60));
        urlService.persistShortLink(url);
        System.out.println(urlService.getEncodedUrl(customShortLink));
    }
    
    //testing via postman
    @PostMapping("/generate")
    public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto) {
        Url urlToRet = urlService.generateShortLink(urlDto);

        if (urlToRet != null) {
            UrlResponseDto urlResponseDto = new UrlResponseDto();
            urlResponseDto.setOriginalUrl(urlToRet.getOriginalUrl());
            urlResponseDto.setExpirationDate(urlToRet.getExpirationDate());
            urlResponseDto.setShortLink(urlToRet.getShortLink());
            System.out.println(urlToRet.getExpirationDate().toString());
            return new ResponseEntity<>(urlResponseDto, HttpStatus.OK);
        }

        UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
        urlErrorResponseDto.setStatus("404");
        urlErrorResponseDto.setError("There was an error processing your request. Please try again.");
        return new ResponseEntity<>(urlErrorResponseDto, HttpStatus.OK);
    }

    @GetMapping("/home/{shortLink}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortLink, HttpServletResponse response)
            throws IOException {

        if (StringUtils.isEmpty(shortLink)) {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Invalid Url");
            urlErrorResponseDto.setStatus("400");
            return new ResponseEntity<>(urlErrorResponseDto, HttpStatus.OK);
        }
        Url urlToRet = urlService.getEncodedUrl(shortLink);

        if (urlToRet == null) {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Url does not exist or it might have expired!");
            urlErrorResponseDto.setStatus("400");
            return new ResponseEntity<>(urlErrorResponseDto, HttpStatus.OK);
        }

        System.out.println(urlToRet.getExpirationDate().toString());
        if (urlToRet.getExpirationDate().isBefore(LocalDateTime.now())) {
            urlService.deleteShortLink(urlToRet);
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Url Expired. Please try generating a fresh one.");
            urlErrorResponseDto.setStatus("200");
            return new ResponseEntity<>(urlErrorResponseDto, HttpStatus.OK);
        }

        response.sendRedirect(urlToRet.getOriginalUrl());
        return null;
    }
}
