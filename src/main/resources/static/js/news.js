
// News Section Slider (Custom)
const list = document.querySelector('.news .list');
const items = document.querySelectorAll('.news .list .item');
const dots = document.querySelectorAll('.news .dots li');
const prev = document.getElementById('prev-btn');
const next = document.getElementById('next-btn');

let active = 0;
let lengthItem = items.length - 1;
let refresh = setInterval(() => next.click(), 3000);

function reloadSlider() {
    const checkleft = items[active].offsetLeft;
    list.style.left = -checkleft + 'px';

    document.querySelector('.news .dots li.active').classList.remove('active');
    dots[active].classList.add('active');

    clearInterval(refresh);
    refresh = setInterval(() => next.click(), 3000);
}

next.onclick = () => {
    active = active === lengthItem ? 0 : active + 1;
    reloadSlider();
};

prev.onclick = () => {
    active = active === 0 ? lengthItem : active - 1;
    reloadSlider();
};

dots.forEach((dot, index) => {
    dot.onclick = () => {
        active = index;
        reloadSlider();
    };
});

// Show arrows on hover
[list, prev, next].forEach(el => {
    el.addEventListener('mouseover', () => {
        prev.style.opacity = 1;
        next.style.opacity = 1;
    });
    el.addEventListener('mouseout', () => {
        prev.style.opacity = 0;
        next.style.opacity = 0;
    });
});
