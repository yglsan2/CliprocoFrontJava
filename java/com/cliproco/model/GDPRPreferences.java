package com.cliproco.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "gdpr_preferences")
public class GDPRPreferences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "analytics_enabled", nullable = false)
    private boolean analyticsEnabled;

    @Column(name = "marketing_enabled", nullable = false)
    private boolean marketingEnabled;

    @Column(name = "preferences_enabled", nullable = false)
    private boolean preferencesEnabled;

    @Column(name = "data_retention_period", nullable = false)
    private int dataRetentionPeriod; // en mois

    @Column(name = "last_consent_date", nullable = false)
    private LocalDateTime lastConsentDate;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "consent_version")
    private String consentVersion;

    public GDPRPreferences() {
        this.lastConsentDate = LocalDateTime.now();
        this.dataRetentionPeriod = 12; // 12 mois par défaut
        this.consentVersion = "1.0";
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAnalyticsEnabled() {
        return analyticsEnabled;
    }

    public void setAnalyticsEnabled(boolean analyticsEnabled) {
        this.analyticsEnabled = analyticsEnabled;
    }

    public boolean isMarketingEnabled() {
        return marketingEnabled;
    }

    public void setMarketingEnabled(boolean marketingEnabled) {
        this.marketingEnabled = marketingEnabled;
    }

    public boolean isPreferencesEnabled() {
        return preferencesEnabled;
    }

    public void setPreferencesEnabled(boolean preferencesEnabled) {
        this.preferencesEnabled = preferencesEnabled;
    }

    public int getDataRetentionPeriod() {
        return dataRetentionPeriod;
    }

    public void setDataRetentionPeriod(int dataRetentionPeriod) {
        this.dataRetentionPeriod = dataRetentionPeriod;
    }

    public LocalDateTime getLastConsentDate() {
        return lastConsentDate;
    }

    public void setLastConsentDate(LocalDateTime lastConsentDate) {
        this.lastConsentDate = lastConsentDate;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getConsentVersion() {
        return consentVersion;
    }

    public void setConsentVersion(String consentVersion) {
        this.consentVersion = consentVersion;
    }

    @Override
    public String toString() {
        return "GDPRPreferences{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", analyticsEnabled=" + analyticsEnabled +
                ", marketingEnabled=" + marketingEnabled +
                ", preferencesEnabled=" + preferencesEnabled +
                ", dataRetentionPeriod=" + dataRetentionPeriod +
                ", lastConsentDate=" + lastConsentDate +
                ", ipAddress='" + ipAddress + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", consentVersion='" + consentVersion + '\'' +
                '}';
    }
} 