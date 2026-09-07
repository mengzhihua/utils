package com.mengzhihua.utils.util;

/**
 * Geographic helpers (WGS84 haversine).
 */
public final class GeoUtil {

    private static final double EARTH_RADIUS_METERS = 6371000D;

    private GeoUtil() {
    }

    public static double distanceMeters(double lat1, double lon1, double lat2, double lon2) {
        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);
        double dPhi = Math.toRadians(lat2 - lat1);
        double dLambda = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dPhi / 2) * Math.sin(dPhi / 2)
                + Math.cos(phi1) * Math.cos(phi2) * Math.sin(dLambda / 2) * Math.sin(dLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_METERS * c;
    }

    public static double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        return distanceMeters(lat1, lon1, lat2, lon2) / 1000D;
    }

    public static boolean inRadius(double lat1, double lon1, double lat2, double lon2, double radiusMeters) {
        return distanceMeters(lat1, lon1, lat2, lon2) <= radiusMeters;
    }
}
