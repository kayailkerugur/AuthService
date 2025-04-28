# AuthService - JWT Authentication Backend

Bu proje, kullanıcı kimlik doğrulaması için geliştirilmiş bir Spring Boot tabanlı backend servisidir.  
JWT (Access Token ve Refresh Token) mimarisi, email doğrulama, şifre sıfırlama ve token blacklist sistemi içermektedir.

---

## 🚀 Özellikler

- Kullanıcı Kayıt (Register) ve Email Doğrulama
- Kullanıcı Giriş (Login) ve JWT Token Üretimi
- Refresh Token ile Token Yenileme
- Şifre Sıfırlama (Forgot Password ve Reset Password)
- Kullanıcı Profil Bilgisi Çekme
- Hesap Silme (30 Gün Bekleme Süresiyle)
- Logout İşlemi ve Redis Tabanlı Token Blacklist Yönetimi
- HTML Formatlı Şık Mail Gönderimi
- Spring Security ve Token Tabanlı Yetkilendirme

---

## 🛠️ Kullanılan Teknolojiler

- Java 17
- Spring Boot 3.4.5
- Spring Security
- JWT (JSON Web Token)
- Redis (In-memory Token Blacklist Sistemi)
- JavaMailSender (SMTP ile Email Gönderimi)
- Liquibase (Database Migration)
- PostgreSQL (Veritabanı)
- Thymeleaf (HTML Mail Template Engine)
- Lombok (Kod Temizliği)
- MapStruct (DTO-Entity Dönüşümleri)

---

## 📦 Projeyi Çalıştırmak İçin

1. Redis Kurulumu

MacOS (Homebrew ile):
```bash
brew install redis
brew services start redis
```

Docker ile Redis çalıştırmak:
```bash 
docker run -d --name redis -p 6379:6379 redis
```

Redis Bağlantısını Test Etmek:
```bash
redis-cli ping
# Beklenen çıktı: PONG
```

✅ Eğer PONG yanıtı alırsanız Redis başarıyla çalışıyor demektir.

2. Maven bağımlılıklarını indir:

```bash
mvn clean install
```
2. ⚙️ Uygulama Ayarları (`application.properties`):
```properties
### 🛢️ Veritabanı Ayarları

```properties
#Veri Tabanı Ayarları
spring.datasource.url=jdbc:postgresql://localhost:5432/authdb
spring.datasource.username=postgres
spring.datasource.password=yourpassword

spring.liquibase.change-log=classpath:/db/changelog/db.changelog-master.yaml
spring.jpa.hibernate.ddl-auto=none

# JWT Ayarları
jwt.secret=your_jwt_secret_key

# Mail Server Ayarları
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_email_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

### Redis Ayarları
spring.data.redis.host=localhost
spring.data.redis.port=6379
# Eğer Redis şifreli ise şu satırı da ekleyin:
# spring.redis.password=your_redis_password
```
3. Uygulamayı başlat:

```bash
mvn spring-boot:run
```
## 📜 API Endpointleri

| Yöntem | URL | Body | Açıklama |
|:---|:---|:---|:---|
| POST | `/api/auth/register` | `{ username, fullName, email, password }` | Yeni kullanıcı kaydı oluşturur ve email doğrulama kodu gönderir. |
| POST | `/api/auth/verify-email` | `{ email, verificationCode }` | Email doğrulama kodu ile kullanıcı hesabını aktif eder. |
| POST | `/api/auth/login` | `{ email, password }` | Giriş yapar ve Access Token + Refresh Token döner. |
| POST | `/api/auth/refresh` | `{ refreshToken }` | Refresh Token kullanarak yeni Access Token üretir. |
| GET | `/api/auth/me` | (Authorization Header gerekli) | Giriş yapmış kullanıcının profil bilgilerini döner. |
| POST | `/api/auth/forgot-password` | `{ email }` | Şifre sıfırlamak için email adresine reset token gönderir. (eski yöntem) |
| POST | `/api/auth/reset-password` | `{ resetToken, newPassword }` | Reset token kullanarak şifreyi sıfırlar. |
| POST | `/api/auth/forgot-password/request-code` | `{ email }` | Şifre sıfırlama kodu gönderir (yeni yöntem, email kod doğrulamalı). |
| POST | `/api/auth/forgot-password/verify-code` | `{ email, code }` | Şifre sıfırlama kodunu doğrular (yeni yöntem). |
| POST | `/api/auth/delete-account` | (Authorization Header gerekli) | Kullanıcı hesabı için 30 gün korumalı silme talebi oluşturur. |
| POST | `/api/auth/logout` | (Authorization Header gerekli) | Kullanıcının Access Token'ını blacklist'e alır ve çıkış yapar. |

---

### 🔑 Authorization

Authorization gereken endpointlerde HTTP Header'a şu şekilde token eklenmelidir:
```bash
Authorization: Bearer {accessToken}
```

## 🔒 Güvenlik Detayları

- **Access Token Süresi:** 1 gün (24 saat)
- **Refresh Token Süresi:** 30 gün
- **Email Doğrulaması:** Email doğrulanmadan giriş yapılamaz.
- **Şifreler:** BCrypt algoritması kullanılarak güvenli şekilde şifrelenir.
- **Logout Sistemi:** Çıkış yapıldığında Access Token blacklist'e alınır ve geçersiz hale gelir.
- **Token Blacklist:** Blacklist'teki tokenlar hiçbir endpoint'e erişemez.
- **Hesap Silme:** Hesap silme isteği oluşturulduktan sonra 30 gün boyunca kurtarılabilir. 30 gün sonunda kalıcı olarak silinir.
- **Şifre Sıfırlama:** Şifre sıfırlamak için hem reset token yöntemi hem de email doğrulama kodu desteklenir.
- **İstek Doğrulama:** Tüm korumalı endpoint'ler Authorization header ile JWT doğrulaması yapar.

## ✨ Geliştirici

- 👤 **İsim:** İlker Uğur Kaya
- 📧 **İletişim:** kayailkercontact@gmail.com

> Bu proje geliştirme, eğitim ve gerçek dünya tecrübesi kazanma amacıyla hazırlanmıştır.  
> Kurumsal uygulamalara rahatlıkla adapte edilebilecek profesyonel bir kimlik doğrulama sistemidir. 🚀

