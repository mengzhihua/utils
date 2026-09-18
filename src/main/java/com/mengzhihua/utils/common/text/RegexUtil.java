package com.mengzhihua.utils.common.text;


import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import com.mengzhihua.utils.common.codec.HexUtil;
import com.mengzhihua.utils.common.net.Ipv6Util;
import com.mengzhihua.utils.common.validate.AbaRoutingUtil;
import com.mengzhihua.utils.common.validate.BicUtil;
import com.mengzhihua.utils.common.validate.CreditCodeUtil;
import com.mengzhihua.utils.common.validate.CusipUtil;
import com.mengzhihua.utils.common.validate.EanUtil;
import com.mengzhihua.utils.common.validate.FigiUtil;
import com.mengzhihua.utils.common.validate.HkIdUtil;
import com.mengzhihua.utils.common.validate.IbanUtil;
import com.mengzhihua.utils.common.validate.ImeiUtil;
import com.mengzhihua.utils.common.validate.IsbnUtil;
import com.mengzhihua.utils.common.validate.IsmnUtil;
import com.mengzhihua.utils.common.validate.Iso6346Util;
import com.mengzhihua.utils.common.validate.LeiUtil;
import com.mengzhihua.utils.common.validate.IsniUtil;
import com.mengzhihua.utils.common.validate.IsinUtil;
import com.mengzhihua.utils.common.validate.IsrcUtil;
import com.mengzhihua.utils.common.validate.IssnUtil;
import com.mengzhihua.utils.common.validate.MacUtil;
import com.mengzhihua.utils.common.validate.CnpjUtil;
import com.mengzhihua.utils.common.validate.CpfUtil;
import com.mengzhihua.utils.common.validate.NhsNumberUtil;
import com.mengzhihua.utils.common.validate.NifUtil;
import com.mengzhihua.utils.common.validate.NpiUtil;
import com.mengzhihua.utils.common.validate.NricUtil;
import com.mengzhihua.utils.common.validate.PeselUtil;
import com.mengzhihua.utils.common.validate.OrcidUtil;
import com.mengzhihua.utils.common.validate.OrgCodeUtil;
import com.mengzhihua.utils.common.validate.PhoneUtil;
import com.mengzhihua.utils.common.validate.SedolUtil;
import com.mengzhihua.utils.common.validate.SirenUtil;
import com.mengzhihua.utils.common.validate.SiretUtil;
import com.mengzhihua.utils.common.validate.TwIdUtil;
import com.mengzhihua.utils.common.validate.UpcEUtil;
import com.mengzhihua.utils.common.validate.CodiceFiscaleUtil;
import com.mengzhihua.utils.common.validate.DoiUtil;
import com.mengzhihua.utils.common.validate.EoriUtil;
import com.mengzhihua.utils.common.validate.IccidUtil;
import com.mengzhihua.utils.common.validate.NirUtil;
import com.mengzhihua.utils.common.validate.PmidUtil;
import com.mengzhihua.utils.common.validate.SteuerIdUtil;
import com.mengzhihua.utils.common.validate.AbnUtil;
import com.mengzhihua.utils.common.validate.FodselsnummerUtil;
import com.mengzhihua.utils.common.validate.HetuUtil;
import com.mengzhihua.utils.common.validate.IswcUtil;
import com.mengzhihua.utils.common.validate.PersonnummerUtil;
import com.mengzhihua.utils.common.validate.SsccUtil;
import com.mengzhihua.utils.common.validate.TfnUtil;
import com.mengzhihua.utils.common.validate.AadhaarUtil;
import com.mengzhihua.utils.common.validate.AhvUtil;
import com.mengzhihua.utils.common.validate.NipUtil;
import com.mengzhihua.utils.common.validate.PanUtil;
import com.mengzhihua.utils.common.validate.PpsUtil;
import com.mengzhihua.utils.common.validate.SinUtil;
import com.mengzhihua.utils.common.validate.VatUtil;
import com.mengzhihua.utils.common.validate.AfmUtil;
import com.mengzhihua.utils.common.validate.CprUtil;
import com.mengzhihua.utils.common.validate.MyNumberUtil;
import com.mengzhihua.utils.common.validate.NinoUtil;
import com.mengzhihua.utils.common.validate.NrnUtil;
import com.mengzhihua.utils.common.validate.PtNifUtil;
import com.mengzhihua.utils.common.validate.RrnUtil;
import com.mengzhihua.utils.common.validate.SvnrUtil;
import com.mengzhihua.utils.common.validate.CuitUtil;
import com.mengzhihua.utils.common.validate.IrdUtil;
import com.mengzhihua.utils.common.validate.MyKadUtil;
import com.mengzhihua.utils.common.validate.RutUtil;
import com.mengzhihua.utils.common.validate.SaIdUtil;
import com.mengzhihua.utils.common.validate.CnpUtil;
import com.mengzhihua.utils.common.validate.EgnUtil;
import com.mengzhihua.utils.common.validate.IcoUtil;
import com.mengzhihua.utils.common.validate.IsraeliIdUtil;
import com.mengzhihua.utils.common.validate.OibUtil;
import com.mengzhihua.utils.common.validate.RegonUtil;
import com.mengzhihua.utils.common.validate.IpnUtil;
import com.mengzhihua.utils.common.validate.IsikukoodUtil;
import com.mengzhihua.utils.common.validate.JmbgUtil;
import com.mengzhihua.utils.common.validate.EmsoUtil;
import com.mengzhihua.utils.common.validate.KennitalaUtil;
import com.mengzhihua.utils.common.validate.LatvianPkUtil;
import com.mengzhihua.utils.common.validate.LithuanianAkUtil;
import com.mengzhihua.utils.common.validate.MxRfcUtil;
import com.mengzhihua.utils.common.validate.NitUtil;
import com.mengzhihua.utils.common.validate.BsnUtil;
import com.mengzhihua.utils.common.validate.CifUtil;
import com.mengzhihua.utils.common.validate.CvrUtil;
import com.mengzhihua.utils.common.validate.OrgnrUtil;
import com.mengzhihua.utils.common.validate.PeDniUtil;
import com.mengzhihua.utils.common.validate.SeOrgNrUtil;
import com.mengzhihua.utils.common.validate.YTunnusUtil;
import com.mengzhihua.utils.common.validate.AdoszamUtil;
import com.mengzhihua.utils.common.validate.CheUidUtil;
import com.mengzhihua.utils.common.validate.CuiUtil;
import com.mengzhihua.utils.common.validate.EdrpouUtil;
import com.mengzhihua.utils.common.validate.EikUtil;
import com.mengzhihua.utils.common.validate.HojinUtil;
import com.mengzhihua.utils.common.validate.KboUtil;
import com.mengzhihua.utils.common.validate.KrBrnUtil;
import com.mengzhihua.utils.common.validate.PibUtil;
import com.mengzhihua.utils.common.validate.TwGuiUtil;
import com.mengzhihua.utils.common.validate.AcnUtil;
import com.mengzhihua.utils.common.validate.GstinUtil;
import com.mengzhihua.utils.common.validate.NpwpUtil;
import com.mengzhihua.utils.common.validate.RegistrikoodUtil;
import com.mengzhihua.utils.common.validate.VknUtil;
import com.mengzhihua.utils.common.validate.IlHpUtil;
import com.mengzhihua.utils.common.validate.LtJaUtil;
import com.mengzhihua.utils.common.validate.InnUtil;
import com.mengzhihua.utils.common.validate.NikUtil;
import com.mengzhihua.utils.common.validate.NzbnUtil;
import com.mengzhihua.utils.common.validate.PeRucUtil;
import com.mengzhihua.utils.common.validate.UenUtil;
import com.mengzhihua.utils.common.validate.EinUtil;
import com.mengzhihua.utils.common.validate.NiptUtil;
import com.mengzhihua.utils.common.validate.OgrnUtil;
import com.mengzhihua.utils.common.validate.SnilsUtil;
import com.mengzhihua.utils.common.validate.CnicUtil;
import com.mengzhihua.utils.common.validate.GhTinUtil;
import com.mengzhihua.utils.common.validate.IdnoUtil;
import com.mengzhihua.utils.common.validate.ItinUtil;
import com.mengzhihua.utils.common.validate.KePinUtil;
import com.mengzhihua.utils.common.validate.CrCpfUtil;
import com.mengzhihua.utils.common.validate.CrCpjUtil;
import com.mengzhihua.utils.common.validate.EgTnUtil;
import com.mengzhihua.utils.common.validate.GtNitUtil;
import com.mengzhihua.utils.common.validate.LuTvaUtil;
import com.mengzhihua.utils.common.validate.MaIceUtil;
import com.mengzhihua.utils.common.validate.MkEdbUtil;
import com.mengzhihua.utils.common.validate.SvNitUtil;
import com.mengzhihua.utils.common.validate.PyRucUtil;
import com.mengzhihua.utils.common.validate.TnMfUtil;
import com.mengzhihua.utils.common.validate.UyRutUtil;
import com.mengzhihua.utils.common.validate.VoenUtil;
import com.mengzhihua.utils.common.validate.RifUtil;
import com.mengzhihua.utils.common.validate.RncUtil;
import com.mengzhihua.utils.common.validate.UnpUtil;
import com.mengzhihua.utils.common.validate.VnMstUtil;
import com.mengzhihua.utils.common.validate.RodneCisloUtil;
import com.mengzhihua.utils.common.validate.TajUtil;
import com.mengzhihua.utils.common.validate.TcKimlikUtil;
import com.mengzhihua.utils.common.validate.ThaiIdUtil;
import com.mengzhihua.utils.common.validate.VinUtil;

/**
 * Common format validators (Hutool {@code Validator} / Commons Validator style).
 */
public final class RegexUtil {

    public static final Pattern MOBILE = Pattern.compile("^1[3-9]\\d{9}$");
    public static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    public static final Pattern ID_CARD = Pattern.compile("^\\d{17}[\\dXx]$");
    public static final Pattern IPV4 = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$");
    public static final Pattern URL = Pattern.compile("^(https?://)[\\w.-]+(?:\\.[\\w.-]+)+(?:[/#?].*)?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern USERNAME = Pattern.compile("^[A-Za-z][A-Za-z0-9_]{3,31}$");
    public static final Pattern CREDIT_CODE = Pattern.compile("^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$");
    public static final Pattern PLATE = Pattern.compile("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-HJ-NP-Z][A-HJ-NP-Z0-9]{4,6}[A-HJ-NP-Z0-9挂学警港澳]$");
    public static final Pattern IPV6 = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
    public static final Pattern ZIPCODE = Pattern.compile("^\\d{6}$");
    public static final Pattern QQ = Pattern.compile("^[1-9]\\d{4,11}$");
    public static final Pattern LANDLINE = Pattern.compile("^0\\d{2,3}-?\\d{7,8}$");
    public static final Pattern UUID = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-8][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$");
    public static final Pattern UUID_SIMPLE = Pattern.compile("^[0-9a-fA-F]{32}$");
    public static final Pattern HEX_COLOR = Pattern.compile("^#(?:[0-9a-fA-F]{3}|[0-9a-fA-F]{6}|[0-9a-fA-F]{8})$");
    public static final Pattern DATE = Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$");
    public static final Pattern TIME = Pattern.compile("^([01]\\d|2[0-3]):[0-5]\\d(:[0-5]\\d)?$");
    public static final Pattern DATETIME = Pattern.compile(
            "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])[ T]([01]\\d|2[0-3]):[0-5]\\d(:[0-5]\\d)?$");
    public static final Pattern CHINESE = Pattern.compile("^[\\u4e00-\\u9fa5]+$");
    public static final Pattern CHINESE_NAME = Pattern.compile("^[\\u4e00-\\u9fa5]{2,8}$");
    public static final Pattern HEX = Pattern.compile("^(?:0x)?[0-9a-fA-F]+$");
    public static final Pattern INTEGER = Pattern.compile("^-?\\d+$");
    public static final Pattern POSITIVE_INT = Pattern.compile("^[1-9]\\d*$");
    public static final Pattern DECIMAL = Pattern.compile("^-?(?:\\d+\\.\\d+|\\d+|\\.\\d+)$");
    public static final Pattern MONEY = Pattern.compile("^(?:0|[1-9]\\d*)(?:\\.\\d{1,2})?$");
    public static final Pattern DOMAIN = Pattern.compile(
            "^(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,}$");
    public static final Pattern PORT = Pattern.compile(
            "^([1-9]\\d{0,3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$");
    public static final Pattern BANK_CARD = Pattern.compile("^\\d{16,19}$");
    public static final Pattern WECHAT = Pattern.compile("^[a-zA-Z][-_a-zA-Z0-9]{5,19}$");
    public static final Pattern PASSPORT = Pattern.compile("^[A-Za-z][A-Za-z0-9]{7,9}$");
    public static final Pattern STRONG_PASSWORD = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
    public static final Pattern JWT = Pattern.compile("^[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+$");
    public static final Pattern BASE64 = Pattern.compile(
            "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$");
    public static final Pattern MD5 = Pattern.compile("^[0-9a-fA-F]{32}$");
    public static final Pattern SHA256 = Pattern.compile("^[0-9a-fA-F]{64}$");
    public static final Pattern SLUG = Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");
    public static final Pattern LETTER = Pattern.compile("^[A-Za-z]+$");
    public static final Pattern ALPHANUM = Pattern.compile("^[A-Za-z0-9]+$");
    public static final Pattern CIDR = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)/(3[0-2]|[12]?\\d)$");
    public static final Pattern LONGITUDE = Pattern.compile("^-?(?:180(?:\\.0+)?|(?:1[0-7]\\d|\\d{1,2})(?:\\.\\d+)?)$");
    public static final Pattern LATITUDE = Pattern.compile("^-?(?:90(?:\\.0+)?|[1-8]?\\d(?:\\.\\d+)?)$");
    public static final Pattern HTML_TAG = Pattern.compile("^</?[A-Za-z][^>]*>$");
    public static final Pattern SEMVER = Pattern.compile("^\\d+\\.\\d+\\.\\d+(?:[-+][0-9A-Za-z.-]+)?$");

    public static final Pattern MOBILE_FIND = Pattern.compile("(?<!\\d)1[3-9]\\d{9}(?!\\d)");
    public static final Pattern EMAIL_FIND = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
    public static final Pattern URL_FIND = Pattern.compile(
            "https?://[\\w.-]+(?:\\.[\\w.-]+)+(?:[/#?][^\\s]*)?", Pattern.CASE_INSENSITIVE);
    public static final Pattern IPV4_FIND = Pattern.compile(
            "(?:(?:25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)");
    public static final Pattern DATE_FIND = Pattern.compile("\\d{4}-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12]\\d|3[01])");
    public static final Pattern HEX_COLOR_FIND = Pattern.compile(
            "#(?:[0-9a-fA-F]{8}|[0-9a-fA-F]{6}|[0-9a-fA-F]{3})(?![0-9A-Fa-f])");
    public static final Pattern ID_CARD_FIND = Pattern.compile("(?<!\\d)\\d{17}[\\dXx](?!\\d)");

    private static final Map<String, java.util.function.Predicate<String>> TYPES = new LinkedHashMap<>();

    static {
        TYPES.put("mobile", RegexUtil::isMobile);
        TYPES.put("email", RegexUtil::isEmail);
        TYPES.put("idcard", RegexUtil::isIdCard);
        TYPES.put("ipv4", RegexUtil::isIpv4);
        TYPES.put("url", RegexUtil::isUrl);
        TYPES.put("username", RegexUtil::isUsername);
        TYPES.put("credit", RegexUtil::isCreditCode);
        TYPES.put("plate", RegexUtil::isPlate);
        TYPES.put("ipv6", RegexUtil::isIpv6);
        TYPES.put("mobilehk", PhoneUtil::isMobileHk);
        TYPES.put("mobiletw", PhoneUtil::isMobileTw);
        TYPES.put("mobilemo", PhoneUtil::isMobileMo);
        TYPES.put("tel400", PhoneUtil::isTel400);
        TYPES.put("zipcode", RegexUtil::isZipcode);
        TYPES.put("qq", RegexUtil::isQq);
        TYPES.put("landline", RegexUtil::isLandline);
        TYPES.put("mac", RegexUtil::isMac);
        TYPES.put("isbn", RegexUtil::isIsbn);
        TYPES.put("uuid", RegexUtil::isUuid);
        TYPES.put("imei", RegexUtil::isImei);
        TYPES.put("iban", RegexUtil::isIban);
        TYPES.put("vin", RegexUtil::isVin);
        TYPES.put("issn", RegexUtil::isIssn);
        TYPES.put("ean", RegexUtil::isEan);
        TYPES.put("isin", RegexUtil::isIsin);
        TYPES.put("bic", RegexUtil::isBic);
        TYPES.put("cusip", RegexUtil::isCusip);
        TYPES.put("sedol", RegexUtil::isSedol);
        TYPES.put("orcid", RegexUtil::isOrcid);
        TYPES.put("isrc", RegexUtil::isIsrc);
        TYPES.put("hexcolor", v -> isMatch(HEX_COLOR, v));
        TYPES.put("date", RegexUtil::isDate);
        TYPES.put("time", RegexUtil::isTime);
        TYPES.put("datetime", RegexUtil::isDateTime);
        TYPES.put("chinese", RegexUtil::isChinese);
        TYPES.put("chinesename", RegexUtil::isChineseName);
        TYPES.put("hex", HexUtil::isHex);
        TYPES.put("integer", RegexUtil::isInteger);
        TYPES.put("positiveint", RegexUtil::isPositiveInt);
        TYPES.put("decimal", RegexUtil::isDecimal);
        TYPES.put("money", RegexUtil::isMoney);
        TYPES.put("domain", RegexUtil::isDomain);
        TYPES.put("port", RegexUtil::isPort);
        TYPES.put("bankcard", RegexUtil::isBankCard);
        TYPES.put("wechat", RegexUtil::isWechat);
        TYPES.put("passport", RegexUtil::isPassport);
        TYPES.put("strongpassword", RegexUtil::isStrongPassword);
        TYPES.put("jwt", RegexUtil::isJwt);
        TYPES.put("base64", RegexUtil::isBase64);
        TYPES.put("md5", RegexUtil::isMd5);
        TYPES.put("sha256", RegexUtil::isSha256);
        TYPES.put("slug", RegexUtil::isSlug);
        TYPES.put("letter", RegexUtil::isLetter);
        TYPES.put("alphanum", RegexUtil::isAlphanum);
        TYPES.put("cidr", RegexUtil::isCidr);
        TYPES.put("longitude", RegexUtil::isLongitude);
        TYPES.put("latitude", RegexUtil::isLatitude);
        TYPES.put("htmltag", RegexUtil::isHtmlTag);
        TYPES.put("semver", RegexUtil::isSemver);
        TYPES.put("hkid", HkIdUtil::isValid);
        TYPES.put("twid", TwIdUtil::isValid);
        TYPES.put("orgcode", OrgCodeUtil::isValid);
        TYPES.put("iso6346", Iso6346Util::isValid);
        TYPES.put("aba", AbaRoutingUtil::isValid);
        TYPES.put("figi", FigiUtil::isValid);
        TYPES.put("lei", LeiUtil::isValid);
        TYPES.put("nhs", NhsNumberUtil::isValid);
        TYPES.put("npi", NpiUtil::isValid);
        TYPES.put("ismn", IsmnUtil::isValid);
        TYPES.put("nric", NricUtil::isValid);
        TYPES.put("cpf", CpfUtil::isValid);
        TYPES.put("cnpj", CnpjUtil::isValid);
        TYPES.put("pesel", PeselUtil::isValid);
        TYPES.put("upce", UpcEUtil::isValid);
        TYPES.put("siren", SirenUtil::isValid);
        TYPES.put("siret", SiretUtil::isValid);
        TYPES.put("nif", NifUtil::isValid);
        TYPES.put("isni", IsniUtil::isValid);
        TYPES.put("nir", NirUtil::isValid);
        TYPES.put("codicefiscale", CodiceFiscaleUtil::isValid);
        TYPES.put("steuerid", SteuerIdUtil::isValid);
        TYPES.put("eori", EoriUtil::isValid);
        TYPES.put("doi", DoiUtil::isValid);
        TYPES.put("pmid", PmidUtil::isValid);
        TYPES.put("iccid", IccidUtil::isValid);
        TYPES.put("personnummer", PersonnummerUtil::isValid);
        TYPES.put("hetu", HetuUtil::isValid);
        TYPES.put("fodselsnummer", FodselsnummerUtil::isValid);
        TYPES.put("iswc", IswcUtil::isValid);
        TYPES.put("sscc", SsccUtil::isValid);
        TYPES.put("abn", AbnUtil::isValid);
        TYPES.put("tfn", TfnUtil::isValid);
        TYPES.put("vat", VatUtil::isValid);
        TYPES.put("ahv", AhvUtil::isValid);
        TYPES.put("nip", NipUtil::isValid);
        TYPES.put("aadhaar", AadhaarUtil::isValid);
        TYPES.put("pan", PanUtil::isValid);
        TYPES.put("sin", SinUtil::isValid);
        TYPES.put("pps", PpsUtil::isValid);
        TYPES.put("cpr", CprUtil::isValid);
        TYPES.put("nrn", NrnUtil::isValid);
        TYPES.put("svnr", SvnrUtil::isValid);
        TYPES.put("ptnif", PtNifUtil::isValid);
        TYPES.put("afm", AfmUtil::isValid);
        TYPES.put("nino", NinoUtil::isValid);
        TYPES.put("rrn", RrnUtil::isValid);
        TYPES.put("mynumber", MyNumberUtil::isValid);
        TYPES.put("rut", RutUtil::isValid);
        TYPES.put("cuit", CuitUtil::isValid);
        TYPES.put("said", SaIdUtil::isValid);
        TYPES.put("ird", IrdUtil::isValid);
        TYPES.put("mykad", MyKadUtil::isValid);
        TYPES.put("tckn", TcKimlikUtil::isValid);
        TYPES.put("israeliid", IsraeliIdUtil::isValid);
        TYPES.put("cnp", CnpUtil::isValid);
        TYPES.put("oib", OibUtil::isValid);
        TYPES.put("egn", EgnUtil::isValid);
        TYPES.put("thaiid", ThaiIdUtil::isValid);
        TYPES.put("regon", RegonUtil::isValid);
        TYPES.put("ico", IcoUtil::isValid);
        TYPES.put("jmbg", JmbgUtil::isValid);
        TYPES.put("isikukood", IsikukoodUtil::isValid);
        TYPES.put("kennitala", KennitalaUtil::isValid);
        TYPES.put("taj", TajUtil::isValid);
        TYPES.put("ipn", IpnUtil::isValid);
        TYPES.put("nit", NitUtil::isValid);
        TYPES.put("lvpk", LatvianPkUtil::isValid);
        TYPES.put("ltak", LithuanianAkUtil::isValid);
        TYPES.put("emso", EmsoUtil::isValid);
        TYPES.put("pedni", PeDniUtil::isValid);
        TYPES.put("mxrfc", MxRfcUtil::isValid);
        TYPES.put("bsn", BsnUtil::isValid);
        TYPES.put("rodne", RodneCisloUtil::isValid);
        TYPES.put("ytunnus", YTunnusUtil::isValid);
        TYPES.put("orgnr", OrgnrUtil::isValid);
        TYPES.put("cvr", CvrUtil::isValid);
        TYPES.put("cif", CifUtil::isValid);
        TYPES.put("orgnrse", SeOrgNrUtil::isValid);
        TYPES.put("cheuid", CheUidUtil::isValid);
        TYPES.put("eik", EikUtil::isValid);
        TYPES.put("rocui", CuiUtil::isValid);
        TYPES.put("adoszam", AdoszamUtil::isValid);
        TYPES.put("kbo", KboUtil::isValid);
        TYPES.put("hojin", HojinUtil::isValid);
        TYPES.put("krbrn", KrBrnUtil::isValid);
        TYPES.put("twgui", TwGuiUtil::isValid);
        TYPES.put("edrpou", EdrpouUtil::isValid);
        TYPES.put("rspib", PibUtil::isValid);
        TYPES.put("gstin", GstinUtil::isValid);
        TYPES.put("acn", AcnUtil::isValid);
        TYPES.put("vkn", VknUtil::isValid);
        TYPES.put("npwp", NpwpUtil::isValid);
        TYPES.put("registrikood", RegistrikoodUtil::isValid);
        TYPES.put("nzbn", NzbnUtil::isValid);
        TYPES.put("uen", UenUtil::isValid);
        TYPES.put("ilhp", IlHpUtil::isValid);
        TYPES.put("ltja", LtJaUtil::isValid);
        TYPES.put("inn", InnUtil::isValid);
        TYPES.put("peruc", PeRucUtil::isValid);
        TYPES.put("nik", NikUtil::isValid);
        TYPES.put("vnmst", VnMstUtil::isValid);
        TYPES.put("ein", EinUtil::isValid);
        TYPES.put("ogrn", OgrnUtil::isValid);
        TYPES.put("snils", SnilsUtil::isValid);
        TYPES.put("nipt", NiptUtil::isValid);
        TYPES.put("rif", RifUtil::isValid);
        TYPES.put("rnc", RncUtil::isValid);
        TYPES.put("unp", UnpUtil::isValid);
        TYPES.put("itin", ItinUtil::isValid);
        TYPES.put("cnic", CnicUtil::isValid);
        TYPES.put("idno", IdnoUtil::isValid);
        TYPES.put("ghtin", GhTinUtil::isValid);
        TYPES.put("kepin", KePinUtil::isValid);
        TYPES.put("maice", MaIceUtil::isValid);
        TYPES.put("voen", VoenUtil::isValid);
        TYPES.put("uyrut", UyRutUtil::isValid);
        TYPES.put("pyruc", PyRucUtil::isValid);
        TYPES.put("gtnit", GtNitUtil::isValid);
        TYPES.put("crcpf", CrCpfUtil::isValid);
        TYPES.put("crcpj", CrCpjUtil::isValid);
        TYPES.put("tnmf", TnMfUtil::isValid);
        TYPES.put("egtn", EgTnUtil::isValid);
        TYPES.put("lutva", LuTvaUtil::isValid);
        TYPES.put("svnit", SvNitUtil::isValid);
        TYPES.put("mkedb", MkEdbUtil::isValid);
    }

    private RegexUtil() {
    }

    public static String normalizeType(String type) {
        return type == null ? "" : type.toLowerCase(Locale.ROOT).replace("-", "").replace("_", "");
    }

    public static java.util.List<String> types() {
        return java.util.List.copyOf(TYPES.keySet());
    }

    public static boolean is(String type, String value) {
        String key = normalizeType(type);
        java.util.function.Predicate<String> check = TYPES.get(key);
        if (check == null) {
            throw new IllegalArgumentException("unsupported type: " + type);
        }
        return check.test(value);
    }

    public static boolean isMatch(Pattern pattern, String value) {
        return value != null && pattern != null && pattern.matcher(value).matches();
    }

    public static boolean isMobile(String value) {
        return isMatch(MOBILE, value);
    }

    public static boolean isEmail(String value) {
        return isMatch(EMAIL, value);
    }

    public static boolean isIdCard(String value) {
        return isMatch(ID_CARD, value);
    }

    public static boolean isIpv4(String value) {
        return isMatch(IPV4, value);
    }

    public static boolean isUrl(String value) {
        return isMatch(URL, value);
    }

    public static boolean isUsername(String value) {
        return isMatch(USERNAME, value);
    }

    public static boolean isCreditCode(String value) {
        return CreditCodeUtil.isValid(value);
    }

    public static boolean isPlate(String value) {
        return isMatch(PLATE, value);
    }

    public static boolean isIpv6(String value) {
        return Ipv6Util.isValid(value);
    }

    public static boolean isZipcode(String value) {
        return isMatch(ZIPCODE, value);
    }

    public static boolean isQq(String value) {
        return isMatch(QQ, value);
    }

    public static boolean isLandline(String value) {
        return isMatch(LANDLINE, value);
    }

    public static boolean isMac(String value) {
        return MacUtil.isValid(value);
    }

    public static boolean isIsbn(String value) {
        return IsbnUtil.isValid(value);
    }

    public static boolean isUuid(String value) {
        return isMatch(UUID, value) || isMatch(UUID_SIMPLE, value);
    }

    public static boolean isImei(String value) {
        return ImeiUtil.isValid(value);
    }

    public static boolean isIban(String value) {
        return IbanUtil.isValid(value);
    }

    public static boolean isVin(String value) {
        return VinUtil.isValid(value);
    }

    public static boolean isIssn(String value) {
        return IssnUtil.isValid(value);
    }

    public static boolean isEan(String value) {
        return EanUtil.isValid(value);
    }

    public static boolean isIsin(String value) {
        return IsinUtil.isValid(value);
    }

    public static boolean isBic(String value) {
        return BicUtil.isValid(value);
    }

    public static boolean isCusip(String value) {
        return CusipUtil.isValid(value);
    }

    public static boolean isSedol(String value) {
        return SedolUtil.isValid(value);
    }

    public static boolean isOrcid(String value) {
        return OrcidUtil.isValid(value);
    }

    public static boolean isIsrc(String value) {
        return IsrcUtil.isValid(value);
    }

    public static boolean isDate(String value) {
        return isMatch(DATE, value);
    }

    public static boolean isTime(String value) {
        return isMatch(TIME, value);
    }

    public static boolean isDateTime(String value) {
        return isMatch(DATETIME, value);
    }

    public static boolean isChinese(String value) {
        return isMatch(CHINESE, value);
    }

    public static boolean isChineseName(String value) {
        return isMatch(CHINESE_NAME, value);
    }

    public static boolean isHex(String value) {
        return isMatch(HEX, value);
    }

    public static boolean isInteger(String value) {
        return isMatch(INTEGER, value);
    }

    public static boolean isPositiveInt(String value) {
        return isMatch(POSITIVE_INT, value);
    }

    public static boolean isDecimal(String value) {
        return isMatch(DECIMAL, value);
    }

    public static boolean isMoney(String value) {
        return isMatch(MONEY, value);
    }

    public static boolean isDomain(String value) {
        return isMatch(DOMAIN, value);
    }

    public static boolean isPort(String value) {
        return isMatch(PORT, value);
    }

    public static boolean isBankCard(String value) {
        return isMatch(BANK_CARD, value);
    }

    public static boolean isWechat(String value) {
        return isMatch(WECHAT, value);
    }

    public static boolean isPassport(String value) {
        return isMatch(PASSPORT, value);
    }

    public static boolean isStrongPassword(String value) {
        return isMatch(STRONG_PASSWORD, value);
    }

    public static boolean isJwt(String value) {
        return isMatch(JWT, value);
    }

    public static boolean isBase64(String value) {
        return isMatch(BASE64, value) && value != null && !value.isEmpty();
    }

    public static boolean isMd5(String value) {
        return isMatch(MD5, value);
    }

    public static boolean isSha256(String value) {
        return isMatch(SHA256, value);
    }

    public static boolean isSlug(String value) {
        return isMatch(SLUG, value);
    }

    public static boolean isLetter(String value) {
        return isMatch(LETTER, value);
    }

    public static boolean isAlphanum(String value) {
        return isMatch(ALPHANUM, value);
    }

    public static boolean isCidr(String value) {
        return isMatch(CIDR, value);
    }

    public static boolean isLongitude(String value) {
        return isMatch(LONGITUDE, value);
    }

    public static boolean isLatitude(String value) {
        return isMatch(LATITUDE, value);
    }

    public static boolean isHtmlTag(String value) {
        return isMatch(HTML_TAG, value);
    }

    public static boolean isSemver(String value) {
        return isMatch(SEMVER, value);
    }

    public static boolean isHexColor(String value) {
        return isMatch(HEX_COLOR, value);
    }
}
