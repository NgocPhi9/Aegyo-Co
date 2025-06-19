const cancelBtns = document.querySelectorAll(".cancel-button");
const banBtns = document.querySelectorAll(".ban-btn");
const editBtns = document.querySelectorAll(".edit-btn");
const overlay = document.getElementById("overlay");

// Lấy tất cả form cancel-confirm
const cancelConfirms = document.querySelectorAll(".cancel-confirm");
const editConfirms = document.querySelectorAll(".edit-confirm");

// Gắn sự kiện cho từng nút hủy
cancelBtns.forEach((btn, index) => {
    btn.addEventListener("click", (event) => {
        event.stopPropagation();
        // Hiện form đúng với dòng đơn hàng đó
        cancelConfirms[index].style.display = "block";
        overlay.style.display = "block";
    });
});

//Edit
editBtns.forEach((btn, index) => {
    btn.addEventListener("click", (event) => {
        event.stopPropagation();
        // Hiện form đúng với dòng đơn hàng đó
        editConfirms[index].style.display = "block";
        overlay.style.display = "block";
    });
});
//Ban account
banBtns.forEach((btn, index) => {
    btn.addEventListener("click", (event) => {
        event.stopPropagation();
        // Hiện form đúng với dòng đơn hàng đó
        cancelConfirms[index].style.display = "block";
        overlay.style.display = "block";
    });
});

cancelConfirms.forEach((form) => {
    form.addEventListener("click", (event) => {
        event.stopPropagation();
    });
});

editConfirms.forEach((form) => {
    form.addEventListener("click", (event) => {
        event.stopPropagation();
    });
});
// Đóng khi click nền mờ
overlay.addEventListener("click", () => {
    cancelConfirms.forEach(form => form.style.display = "none");
    editConfirms.forEach(form => form.style.display = "none");
    overlay.style.display = "none";
});
