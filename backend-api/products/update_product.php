<?php
require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../config/response.php';
require_once __DIR__ . '/../config/auth.php';

getAuthUser();
$id = (int)input('id', 0);
$name = input('name');
$description = input('description');
$price = (int)input('price', 0);
$originCity = input('origin_city');
$etaDays = (int)input('eta_days', 0);
$isActive = (int)input('is_active', 1) === 1 ? 1 : 0;

if ($id <= 0 || $name === '' || $price <= 0 || $originCity === '' || $etaDays <= 0) {
    jsonResponse(false, 'Data produk tidak valid', null, 422);
}

$pdo = getPDO();
$stmt = $pdo->prepare('UPDATE products SET name=?, description=?, price=?, origin_city=?, eta_days=?, is_active=?, updated_at=NOW() WHERE id=?');
$stmt->execute([$name, $description, $price, $originCity, $etaDays, $isActive, $id]);
jsonResponse(true, 'Produk berhasil diupdate');
?>
