function showTab(tab) {
  const reviewTab = document.getElementById("reviewTab");
  const commentTab = document.getElementById("commentTab");
  const reviewContent = document.getElementById("reviewContent");
  const commentContent = document.getElementById("commentContent");
  const contentContainer = document.getElementById("contentContainer");

  if (tab === "review") {
    reviewTab.classList.add("active-review");
    commentTab.classList.remove("active-comment");
    reviewContent.classList.add("visible");
    commentContent.classList.remove("visible");
    contentContainer.classList.add("bg-review");
    contentContainer.classList.remove("bg-comment");
  } else {
    commentTab.classList.add("active-comment");
    reviewTab.classList.remove("active-review");
    commentContent.classList.add("visible");
    reviewContent.classList.remove("visible");
    contentContainer.classList.add("bg-comment");
    contentContainer.classList.remove("bg-review");
  }
}
