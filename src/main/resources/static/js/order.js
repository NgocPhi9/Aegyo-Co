const form = document.getElementById("ratingForm");
const alertDiv = document.getElementById("alert");

form.addEventListener("submit", function (e) {
  e.preventDefault();
  const rating = form.rating.value;
  const comment = form.comment.value.trim();

  if (!rating) {
    alertDiv.innerHTML = "Vui lòng chọn số sao!";
    return;
  }

  if (!comment) {
    alertDiv.innerHTML = "Vui lòng viết bình luận!";
  }
});

const reviewBtn = document.querySelector(".review-btn");
const cancelBtn = document.querySelector(".cancel-btn");
const ratingForm = document.querySelector(".rating-container");
const cancelConfirm = document.querySelector(".cancel-confirm");
const overlay = document.getElementById("overlay");

reviewBtn.addEventListener("click", () => {
  ratingForm.style.display = "block";
  overlay.style.display = "block";
});

cancelBtn.addEventListener("click", () => {
  cancelConfirm.style.display = "block";
  overlay.style.display = "block";
});

// Ấn nền mờ để đóng form
overlay.addEventListener("click", () => {
  ratingForm.style.display = "none";
  cancelConfirm.style.display = "none";
  overlay.style.display = "none";
});