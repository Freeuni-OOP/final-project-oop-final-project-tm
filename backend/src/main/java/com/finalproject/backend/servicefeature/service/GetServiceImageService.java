package com.finalproject.backend.servicefeature.service;

import com.finalproject.backend.servicefeature.dao.MockGetServiceImageDao;
import org.springframework.stereotype.Service;

@Service
public class GetServiceImageService {

    private final MockGetServiceImageDao serviceImageDao;

    // Spring injects the MockServiceImageDao here automatically
    public GetServiceImageService(MockGetServiceImageDao serviceImageDao) {
        this.serviceImageDao = serviceImageDao;
    }

    public byte[] downloadImage(String id) {
        return serviceImageDao.getImage(id);
    }

    public String getContentType(String id) {
        return serviceImageDao.getContentType(id);
    }
}