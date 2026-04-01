# JastiProOps Android

## Buka project
1. Buka Android Studio
2. Open folder `JastiProOps`
3. Sync Gradle

## Ubah Base URL
File: `app/src/main/java/com/example/jastiproops/api/ApiClient.kt`
- Emulator: `http://192.168.0.22/jastip_internal_api/`
- HP fisik: `http://192.168.0.22/jastip_internal_api/`

## Run di Emulator
- Pastikan XAMPP Apache+MySQL aktif
- Import SQL backend
- Jalankan app

## Run di HP Fisik
- HP dan laptop satu WiFi
- Ganti BASE_URL ke IP laptop (contoh 192.168.0.22)
- Izinkan HTTP lokal (sudah via network_security_config)

## Akun default
- Manager: manager@jastipro.local / manager123
- Petugas: petugas@jastipro.local / petugas123

