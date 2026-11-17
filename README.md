# Amet FPS Booster

Minecraft **26.2** için geliştirilmiş, Fabric tabanlı istemci FPS optimizasyon modu.

Amet FPS Booster; oyun içindeki performans seçeneklerini tek bir menüden yönetmeyi ve farklı kullanım senaryoları için hazır performans profilleri seçmeyi amaçlar.

## Özellikler

- **Hızlı menü:** Sağ Shift tuşuyla mod menüsünü açma.
- **Hazır profiller:** Dengeli, Max, EXTREME ve Kapalı seçenekleri.
- **Modül kontrolleri:** Performans seçeneklerini menü üzerinden açıp kapatma.
- **Adaptif render distance:** Görüş mesafesini performansa göre ayarlama seçeneği.
- **Arka plan FPS limiti:** Oyun arka plandayken FPS sınırını yönetme.
- **Hedef FPS:** İstenen FPS hedefini ayarlama.

> Not: Performans kazanımı bilgisayarın donanımına, Minecraft ayarlarına ve kurulu modlara göre değişebilir. Her sistemde aynı sonucu garanti etmez.

## Gereksinimler

- Minecraft **26.2**
- Java **25 veya üzeri**
- Fabric Loader **0.19.3 veya üzeri**
- Fabric API
- **Sodium (zorunlu)**

Aşağıdaki modlar isteğe bağlıdır ve uyumlu sürümleri kullanılması önerilir:

- Lithium
- FerriteCore
- ImmediatelyFast
- Entity Culling

## Kurulum

1. Minecraft 26.2 için Fabric Loader'ı kur.
2. Fabric API ve Sodium'un Minecraft sürümünle uyumlu sürümlerini indir.
3. Amet FPS Booster JAR dosyasını ve gerekli modları Minecraft'ın `mods` klasörüne koy.
4. Oyunu Fabric profiliyle başlat.
5. Menüye erişmek için **Sağ Shift** tuşuna bas.

## Kaynak koddan derleme

Bu projeyi derlemek için Java 25 veya üzeri gerekir.

Proje klasörüne (`amet-fps-booster`) gir ve işletim sistemine göre şu komutu çalıştır:

**Windows**
```bat
gradlew.bat build
```

**Linux / macOS**
```bash
./gradlew build
```

Başarılı derlemeden sonra JAR dosyası şu klasörde oluşur:

```
amet-fps-booster/build/libs/
```

## Katkı ve geri bildirim

Hata bildirimleri ve geliştirme önerileri için GitHub deposundaki **Issues** bölümünü kullanabilirsin.

## Teşekkür

Bu projenin temelini oluşturan [Amet-FPS-Boost](https://github.com/amet265/Amet-FPS-Boost) projesinin geliştiricisi **@bnAmet0**'a ve projeye katkı sağlayan **@ItssPoison**'a teşekkürler.

---

**Amet FPS Booster** — Fabric tabanlı, FPS odaklı Minecraft istemci modu.
