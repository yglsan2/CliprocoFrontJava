package com.cliproco.service;

import com.cliproco.dao.GDPRPreferencesDao;
import com.cliproco.model.GDPRPreferences;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GDPRService {
    
    @Autowired
    private GDPRPreferencesDao gdprPreferencesDao;

    @Transactional
    public GDPRPreferences saveConsent(String userId, boolean analyticsEnabled, boolean marketingEnabled, 
                                     boolean preferencesEnabled, HttpServletRequest request) {
        GDPRPreferences preferences = new GDPRPreferences();
        preferences.setUserId(userId);
        preferences.setAnalyticsEnabled(analyticsEnabled);
        preferences.setMarketingEnabled(marketingEnabled);
        preferences.setPreferencesEnabled(preferencesEnabled);
        preferences.setLastConsentDate(LocalDateTime.now());
        preferences.setIpAddress(request.getRemoteAddr());
        preferences.setUserAgent(request.getHeader("User-Agent"));
        
        return gdprPreferencesDao.create(preferences);
    }

    @Transactional(readOnly = true)
    public Optional<GDPRPreferences> getUserPreferences(String userId) {
        return gdprPreferencesDao.findByUserId(userId);
    }

    @Transactional
    public void updateConsent(Long preferencesId, boolean analyticsEnabled, boolean marketingEnabled, 
                            boolean preferencesEnabled) {
        Optional<GDPRPreferences> preferencesOpt = gdprPreferencesDao.findById(preferencesId);
        if (preferencesOpt.isPresent()) {
            GDPRPreferences preferences = preferencesOpt.get();
            preferences.setAnalyticsEnabled(analyticsEnabled);
            preferences.setMarketingEnabled(marketingEnabled);
            preferences.setPreferencesEnabled(preferencesEnabled);
            preferences.setLastConsentDate(LocalDateTime.now());
            gdprPreferencesDao.update(preferences);
        }
    }

    @Transactional
    public void deleteUserData(String userId) {
        Optional<GDPRPreferences> preferencesOpt = gdprPreferencesDao.findByUserId(userId);
        if (preferencesOpt.isPresent()) {
            gdprPreferencesDao.delete(preferencesOpt.get());
        }
    }

    @Transactional
    public void updateDataRetentionPeriod(Long preferencesId, int months) {
        Optional<GDPRPreferences> preferencesOpt = gdprPreferencesDao.findById(preferencesId);
        if (preferencesOpt.isPresent()) {
            GDPRPreferences preferences = preferencesOpt.get();
            preferences.setDataRetentionPeriod(months);
            gdprPreferencesDao.update(preferences);
        }
    }

    public boolean hasValidConsent(String userId) {
        Optional<GDPRPreferences> preferencesOpt = gdprPreferencesDao.findByUserId(userId);
        if (preferencesOpt.isPresent()) {
            GDPRPreferences preferences = preferencesOpt.get();
            LocalDateTime expirationDate = preferences.getLastConsentDate()
                    .plusMonths(preferences.getDataRetentionPeriod());
            return LocalDateTime.now().isBefore(expirationDate);
        }
        return false;
    }
} 