
### Spring Boot use Http/2

#### generate keystore

```
keytool -genkey -alias undertow -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -dname "CN=localhost, OU=localhost, O=localhost, L=Zhengzhou, ST=Henan, C=CN"
输入密钥库口令:
再次输入新口令:
```

#### if you want to open both https and http ports

add UndertowHttpConfiguration Configuration (application will start with only https port by default)

### reference

https://blog.csdn.net/jaune161/article/details/82879044

https://docs.spring.io/spring-boot/docs/2.1.7.RELEASE/reference/htmlsingle/#howto-configure-ssl