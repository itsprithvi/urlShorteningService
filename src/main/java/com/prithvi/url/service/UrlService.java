package com.prithvi.url.service;


import org.springframework.stereotype.Service;

import com.prithvi.url.model.Url;
import com.prithvi.url.model.UrlDto;



@Service
public interface UrlService
{
    public Url generateShortLink(UrlDto urlDto);
    public Url persistShortLink(Url url);
    public Url getEncodedUrl(String url);
    public  void  deleteShortLink(Url url);
}
