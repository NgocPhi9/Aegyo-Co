// const form = document.getElementById("ratingForm");
// const alertDiv = document.getElementById("alert");
//
// if (form) {
//   form.addEventListener("submit", function (e) {
//     const rating = form.rating.value;
//     const comment = form.comment.value.trim();
//
//     if (!rating) {
//       alertDiv.innerHTML = "Vui lòng chọn số sao!";
//       return;
//     }
//
//     if (!comment) {
//       alertDiv.innerHTML = "Vui lòng viết bình luận!";
//     }
//   });
// }

const allReviewButtons = document.querySelectorAll(".review-btn");
// const reviewBtn = document.querySelector(".review-btn");
const cancelBtn = document.querySelector(".cancel-btn");
const editBtn = document.querySelector(".edit-btn");
const ratingForm = document.querySelector(".rating-container");
const cancelConfirm = document.querySelector(".cancel-confirm");
const editConfirm = document.querySelector(".edit-confirm");
const overlay = document.getElementById("overlay");
//
// if (reviewBtn) {
//   reviewBtn.addEventListener("click", () => {
//     ratingForm.style.display = "block";
//     overlay.style.display = "block";
//   });
// }

allReviewButtons.forEach(button => {
  button.addEventListener("click", (event) => {
    // Find the rating container next to the button that was clicked
    const itemContainer = event.target.closest('.control-item');
    const ratingContainer = itemContainer.nextElementSibling; // The rating-container div

    if (ratingContainer) {
      ratingContainer.style.display = "block";
      overlay.style.display = "block";
    }
  });
});

cancelBtn.addEventListener("click", () => {
  cancelConfirm.style.display = "block";
  overlay.style.display = "block";
});

editBtn.addEventListener("click", () => {
  editConfirm.style.display = "block";
  overlay.style.display = "block";
});

// Ấn nền mờ để đóng form
overlay.addEventListener("click", () => {
  document.querySelectorAll('.rating-container').forEach(popup => {
    popup.style.display = "none";})
  cancelConfirm.style.display = "none";
  editConfirm.style.display = "none";
  overlay.style.display = "none";
});