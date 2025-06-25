const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

function formatCurrencyVND(amount) {
  return amount.toLocaleString('vi-VN') + ' ₫';
}

//update số lượng ở header-cart
function updateCartCount() {
  $.ajax({
    url: '/4Moos/private/cart/quantity',
    type: 'GET',
    success: function (quantity) {
      const badge = $('.cart-quantity');
      if (quantity > 0) {
        badge.text(quantity).removeClass('hidden');
      } else {
        badge.addClass('hidden');
      }
    },
    error: function (error) {
      alert("Không thể lấy thông tin giỏ hàng.");
    }
  })
}

function increaseQty(btn) {
  let quantityInput = btn.previousElementSibling;
  let quantity = parseInt(quantityInput.value);
  if (quantity < 20) {
    quantityInput.value = quantity + 1;
  }
}

function decreaseQty(btn) {
  let quantityInput = btn.nextElementSibling;
  let quantity = parseInt(quantityInput.value);

  if (quantity > 1) {
    quantityInput.value = quantity - 1;
  }
}

function changeQty(input) {
  let quantity = parseInt(input.value);

  if (isNaN(quantity) || quantity <= 0 || quantity > 20) {
    input.value = 1;
  }
}

function getCheckedIdFromURL() {
  const params = new URLSearchParams(window.location.search);
  return params.get('checkedId');
}

function buyNow(button) {
  const idProduct = button.getAttribute("data-id");
  const qtyInput = document.querySelector(".quantity");
  const quantity = qtyInput ? parseInt(qtyInput.value) : 1;

  fetch('/4Moos/cart/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      [header]: token // thêm header CSRF
    },
    body: JSON.stringify({
      idProduct: idProduct,
      quantity: quantity
    })
  })
      .then(response => {
        if (response.ok) {
          window.location.href = `/4Moos/cart?checkedId=${idProduct}`;
          updateCartCount();
        } else {
          showToast("Thêm vào giỏ hàng thất bại!");
        }
      });
}

function addToCart(event) {
  event.preventDefault(); // Ngăn không cho click vào nút bị chuyển link
  event.stopPropagation(); // Ngăn không cho sự kiện lan ra thẻ <a>

  const button = event.currentTarget;
  const idProduct = button.getAttribute("data-id");

  const qtyInput = document.querySelector(".quantity");
  const quantity = qtyInput ? parseInt(qtyInput.value) : 1;

  fetch('/4Moos/cart/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      [header]: token // thêm header CSRF
    },
    body: JSON.stringify({
      idProduct: idProduct,
      quantity: quantity
    })
  })
      .then(response => {
        if (response.ok) {
          showToast("Thêm vào giỏ hàng thành công!");
          updateCartCount();
        } else {
          showToast("Thêm vào giỏ hàng thất bại!");
        }
      });
}

function updateCart(element) {
  const input = element.parentNode.querySelector('input');
  const quantity = parseInt(input.value);
  const checkbox = element.closest('.item').querySelector('.product-checkbox');
  const idProduct = checkbox.getAttribute('data-id');

  const available = parseInt(input.dataset.available);
  const quantityAlert = element.closest('.item').querySelector('.quantity-alert');
  if (quantity > available) {
    quantityAlert.textContent = `Sản phầm còn ${available}`;
    quantityAlert.style.display = 'block';
    checkbox.disabled = true;
    checkbox.checked = false; // bỏ chọn nếu đã chọn
  } else {
    quantityAlert.style.display = 'none';
    checkbox.disabled = false;
  }

  fetch('/4Moos/cart/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      [header]: token // thêm header CSRF
    },
    body: JSON.stringify({ idProduct: idProduct, quantity: quantity })
  })
      .then(response => {
        if (response.ok) {
          updateCartCount();
          updateTotal();
        } else {
          alert("Không thể thêm");
        }
      })
      .catch(() => alert("Có lỗi xảy ra khi thêm sản phẩm."));
}

function removeItem(button) {
  const checkbox = button.closest('.item').querySelector('.product-checkbox');
  const idProduct = checkbox.getAttribute("data-id");

  fetch(`/4Moos/cart/remove?idProduct=${idProduct}`, {
    method: 'POST',
    headers: {
      [header]: token
    }
  })
      .then(response => {
        if (response.ok) {
          button.closest('.item').remove();
          updateCartCount();
          updateTotal();
          updateBuyButtonState();
        } else {
          alert("Không thể xóa");
        }
      })
      .catch(() => alert("Có lỗi xảy ra khi xóa sản phẩm."));
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

function updateTotal() {
  let totalAmount = 0;

  document.querySelectorAll('.item').forEach(item => {
    const checkbox = item.querySelector('.product-checkbox');
    const unitPrice = parseFloat(checkbox.dataset.price);
    const quantity = parseInt(item.querySelector('.quantity').value);

    const totalPrice = unitPrice * quantity;

    checkbox.closest('.item').querySelector('.total-price').textContent = formatCurrencyVND(totalPrice);

    if (checkbox.checked) {
      totalAmount += totalPrice;
    }
  });
  document.getElementById('total-amount').innerText = formatCurrencyVND(totalAmount);
}

function prepareOrder(event) {
  const selected = [];
  document.querySelectorAll('.product-checkbox:checked').forEach(cb => {
    const id = cb.getAttribute('data-id');
    const quantityInput = cb.closest('.item').querySelector('.quantity');
    const quantity = quantityInput ? parseInt(quantityInput.value) : 0;

    if (id && quantity > 0) {
      selected.push({ idProduct: id, quantity: quantity });
    }
  });

  document.getElementById('formItemsJson').value = JSON.stringify(selected);
  document.querySelector('.checkout-form').submit();
}

function updateBuyButtonState() {
  const selected = document.querySelectorAll('.product-checkbox:checked').length > 0;
  const buyButton = document.getElementById('buy-btn');
  if (!buyButton) {
    const isBanned = buyButton.getAttribute('data-isBanned') === 'true';
  }
  buyButton.disabled = !selected;
}

const checkedFromURL = getCheckedIdFromURL();
if (checkedFromURL) {
  const checkbox = document.querySelector(`.product-checkbox[data-id="${checkedFromURL}"]`);
  if (checkbox) checkbox.checked = true;
}

document.querySelectorAll('.product-checkbox').forEach(checkbox => {
  checkbox.addEventListener('change', updateBuyButtonState);
});

updateTotal();
updateBuyButtonState();