package so.sao.shop.supplier.alipay;

/**
 * @author gxy on 2017/9/15.
 */
public class AlipayConfig {
    //网关地址
    public static String alipayurl = "https://openapi.alipay.com/gateway.do";
    //APPID
    public static String appid = "2017011705155123";
    //私钥
    public static String private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCzjVBdLDaJYYVn\n" +
                                        "P4XfLJA0WkWkRuDxQBkNRYmtCqZbKaEah/Q/aYlZS8YaBSJaQhpHBzdTmJ/y6uOj\n" +
                                        "zWr5xg6IqMSKF1PdMiJVRDxW0v3s3Dbqgkah98Nrq+ONv1nDpvxoMiqifftY7Q/V\n" +
                                        "QiRR1smkOyvaPP5ZxPOU3MAjrFwaObdjgI/ZgNjbASZVUv0HZQzJ1hcmIqh3N0Iv\n" +
                                        "wzorNIUHpl4AVevuBTzMA8PbJOyQ6oYgmeWnnws4VUsfle+n2FmAhLTUA50MtVXJ\n" +
                                        "sDQ/zo5hsM1Bdqi/+3N15SzjZ1ue99PTyuoHhjeVolXEe80kI7qB42DySqqg+mEe\n" +
                                        "q2KEmy8RAgMBAAECggEAHv0hxKLv2rAhJRu6kDURwmdeI6c0BY9Jg5ff1iTDZ3J8\n" +
                                        "4qZaSmN9mBLS4wTF1Awamu2s59msqXFOzqOSdf9v3Lg8C5VRHRkdAdhgDaAmwS2U\n" +
                                        "sM7BQA0SBlBJWtqOtIzqfVVDNH5H1iY8GFNtD07hyF2KCQWD5Jxwad3br4x87O2E\n" +
                                        "yCeOFCXW698BnQMMmPlCs7jMMz+IxMReStPqWhuGx2HDpUyZMdNO7g6fKZ8Bk3jh\n" +
                                        "ZwKbCzMoXGVkTukigleee04UooC2jucKWwXuCPWx9WIrOH++h3uCqt024McIZqpq\n" +
                                        "y6AuVtpwi8xCX+u7i6hNv9t+J2mek07k0z8NSx5UnQKBgQDt5f/4VPLRWdA4JHg0\n" +
                                        "QYHxp3SRm09VUDujb/Uekjbmd+GoDCakSk3FWesOuyqf5S2ASaDuxh+aH0OQZkGk\n" +
                                        "h8fR4fLIBb2hwl2+SVhAVB6tr1FBDSkrRiwtrmToXnoZg24v4CMSe3XzctQBfxK4\n" +
                                        "s04DbwM0mfdfKKXte0zcNK+5PwKBgQDBNsoqesGy0bKg1NZP0pZdihiOHnz1IO/T\n" +
                                        "um+5jTlwxwaGw1CbWUncfjcZM6obXZ9DcuPS94mi8g5Lv5DfPxGTKfPy/nNngbYO\n" +
                                        "L73YMqtxi44W+UbtDPHf8hlFfh3Ha/qXKcs9kz4/PeytCHUzU2SWRm2D2IAQFUcp\n" +
                                        "yLdYqGwzrwKBgBz6aj6mollL9mgQUTAUVq7H9hQSdgxvVIRpC9OofdV+/tbIfCCB\n" +
                                        "CI4kkNRfiial9O/uiBLtwUW1zzYUrqRLAdUYD8m7oUjF80NNjdlwJbiEZYJmBwDF\n" +
                                        "cHY0CwifEYJwFwjv5XXH4itAboCsKOyksrkKWbqbkp1dxzBxV4BvToRjAoGAWAwI\n" +
                                        "uqzwIfXvad0zwHJdtUieAf2gwe/9ekXGbg8OMvdvYPAWhoR85S7bGu/xP8E9N3Ze\n" +
                                        "nZ/SJ+AYZappcWKDNsXydjRAENYKSZK195S3jemjmZKFILc6bewcWg+rsFnjewk/\n" +
                                        "pdK4lgc5GGz3Q6ln+BcmBuQdD7cyk7b03kD59d8CgYBLNWJYm0iXT3ciuA6futEa\n" +
                                        "KFs8qcHsMtlJIl2G4+ZaktMTh/HrYpLy56mneJInn9h9BQ1IocpUqDh0MGGMtTKK\n" +
                                        "MUDmh5C708eOMan0Xxyct4edPTvtHiEoUllN5vlK1WfSQbBXF25fQ5va2mEFZKMp\n" +
                                        "fOpoBwTzRkwF0FXw+jetGw==";
    public static String content_type = "RSA2"; //签名方式
    public static String input_charset = "utf-8";   //字符编码格式
    //支付宝公钥
    public static String ali_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh1BW9frgeJn38zG6LoWizYDGrfpZ4AuGFK21WajBTdz3lZ2zZsh1QhPqqBknYoLzEAL8nixnTVn0D20rXaQgh53n1TG9TGWlrZ0II2rA+KaEBmDy9yfUk1J7Ozsp/bEOddLN5tw/VXz7ous7rfDiQ7PKo++rapWJ8k5z/bfl6SPOZPftrUNHDgFscLUhtsI8vX0aCFSwB8mGUniCGQjxTMyv8G5ARfDyju7Iwf7TIrNkANex7WpTBM23CC5ikpG4lIb6Mhfmyyez7PSWqXpylyTZ4B8+kEKc4JLroay0suY2KmwFe9IY+/5Ni86niy/RMOvj4FZjBH5fozep4YsCmwIDAQAB";
}
