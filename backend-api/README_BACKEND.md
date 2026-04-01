# JastiProOps Backend (PHP Native)

## Lokasi folder
Taruh semua isi folder ini di:
`C:/xampp/htdocs/jastip_internal_api/`

## Buat database
1. Buka phpMyAdmin
2. Import file SQL: `database_jastip_internal.sql`
3. Pastikan nama DB: `jastip_internal`

## Konfigurasi koneksi
Edit `config/database.php` jika user/password MySQL berbeda.

## Base URL default
`http://192.168.0.22/jastip_internal_api/`

## Endpoint utama
- `auth/login.php`
- `reports/dashboard_summary.php`
- `customers/*.php`
- `products/*.php`
- `orders/*.php`
- `users/*.php`
- `reports/simple_report.php`

## Akun default
- Manager: `manager@jastipro.local` / `manager123`
- Petugas: `petugas@jastipro.local` / `petugas123`
