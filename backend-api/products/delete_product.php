<?php
require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../config/response.php';
require_once __DIR__ . '/../config/auth.php';

getAuthUser();
$id = (int)input('id', 0);
if ($id <= 0) {
    jsonResponse(false, 'ID produk tidak valid', null, 422);
}
$pdo = getPDO();
$stmt = $pdo->prepare('UPDATE products SET is_active=0, updated_at=NOW() WHERE id=?');
$stmt->execute([$id]);
jsonResponse(true, 'Produk berhasil dihapus (soft delete)');
?>
