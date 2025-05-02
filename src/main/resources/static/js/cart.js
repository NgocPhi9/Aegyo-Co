function increaseQty(btn) {
  let quantityInput = btn.previousElementSibling;
  let quantity = parseInt(quantityInput.value);
  quantityInput.value = quantity + 1;
}

function decreaseQty(btn) {
  let quantityInput = btn.nextElementSibling;
  let quantity = parseInt(quantityInput.value);

  if (quantity > 1) {
    quantityInput.value = quantity - 1;
  } else {
    let cartItem = btn.closest(".item");
    cartItem.remove();
  }
}

function changeQty(input) {
  let quantity = parseInt(input.value);

  if (isNaN(quantity) || quantity <= 0) {
    input.value = 1;
  }
}

function addToCart(event) {
  event.preventDefault(); // Ngăn không cho click vào nút bị chuyển link
  event.stopPropagation(); // Ngăn không cho sự kiện lan ra thẻ <a>

  const button = event.currentTarget;
  const id = button.getAttribute("data-id");

  const qtyInput = document.querySelector(".quantity");
  const quantity = qtyInput ? parseInt(qtyInput.value) : 1;

  let cart = JSON.parse(localStorage.getItem("cart")) || [];

  const existingItem = cart.find((item) => item.id === id);
  if (existingItem) {
    existingItem.quantity += quantity;
  } else {
    cart.push({ id: id, quantity: quantity });
  }

  localStorage.setItem("cart", JSON.stringify(cart));
  showToast("Đã thêm sản phẩm vào giỏ hàng!");
}

function showToast(message) {
  const toast = document.createElement("div");
  toast.className = "toast";
  toast.textContent = message;
  document.getElementById("toast-container").appendChild(toast);

  setTimeout(() => {
    toast.style.opacity = "0";
    // Sau khi ẩn (0.5s), xóa hẳn
    setTimeout(() => toast.remove(), 500);
  }, 1500);
}
