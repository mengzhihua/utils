package com.mengzhihua.utils.common.extra;

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

    public static double[] wgs84ToGcj02(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[] {lat, lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * Math.PI);
        dLon = (dLon * 180.0) / (A / sqrtMagic * Math.cos(radLat) * Math.PI);
        return new double[] {lat + dLat, lon + dLon};
    }

    public static double[] gcj02ToWgs84(double lat, double lon) {
        double[] gps = wgs84ToGcj02(lat, lon);
        return new double[] {lat * 2 - gps[0], lon * 2 - gps[1]};
    }

    public static double[] gcj02ToBd09(double lat, double lon) {
        double z = Math.sqrt(lon * lon + lat * lat) + 0.00002 * Math.sin(lat * Math.PI * 3000.0 / 180.0);
        double theta = Math.atan2(lat, lon) + 0.000003 * Math.cos(lon * Math.PI * 3000.0 / 180.0);
        return new double[] {z * Math.sin(theta) + 0.006, z * Math.cos(theta) + 0.0065};
    }

    public static double[] bd09ToGcj02(double lat, double lon) {
        double x = lon - 0.0065;
        double y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI * 3000.0 / 180.0);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI * 3000.0 / 180.0);
        return new double[] {z * Math.sin(theta), z * Math.cos(theta)};
    }

    public static boolean outOfChina(double lat, double lon) {
        return lon < 72.004 || lon > 137.8347 || lat < 0.8293 || lat > 55.8271;
    }

    private static final double A = 6378245.0;
    private static final double EE = 0.00669342162296594323;

    private static double transformLat(double x, double y) {
        double result = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        result += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        result += (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0 * Math.PI)) * 2.0 / 3.0;
        result += (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y * Math.PI / 30.0)) * 2.0 / 3.0;
        return result;
    }

    private static double transformLon(double x, double y) {
        double result = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        result += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        result += (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0 * Math.PI)) * 2.0 / 3.0;
        result += (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x / 30.0 * Math.PI)) * 2.0 / 3.0;
        return result;
    }
}
