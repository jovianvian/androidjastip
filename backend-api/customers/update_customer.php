<?php
require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../config/response.php';
require_once __DIR__ . '/../config/auth.php';

getAuthUser();
$id = (int)input('id', 0);
$name = input('name');
$phone = input('phone');
$address = input('address');
$notes = input('notes');

if ($id <= 0 || $name === '' || $phone === '' || $address === '') {
    jsonResponse(false, 'Data tidak lengkap', null, 422);
}

$pdo = getPDO();
$stmt = $pdo->prepare('SELECT id FROM customers WHERE id=? AND is_active=1 LIMIT 1');
$stmt->execute([$id]);
if (!$stmt->fetch()) {
    jsonResponse(false, 'Customer tidak ditemukan', null, 404);
}

$stmt = $pdo->prepare('UPDATE customers SET name=?, phone=?, address=?, notes=?, updated_at=NOW() WHERE id=?');
$stmt->execute([$name, $phone, $address, $notes, $id]);
jsonResponse(true, 'Customer berhasil diupdate');
?>
