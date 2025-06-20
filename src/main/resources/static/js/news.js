// This function can be called on any slider element to make it work
function initializeSlider(sliderElement) {
    const list = sliderElement.querySelector('.list');
    const items = sliderElement.querySelectorAll('.list .item');
    const dotsContainer = sliderElement.querySelector('.dots');
    const prev = sliderElement.querySelector('.prev-btn');
    const next = sliderElement.querySelector('.next-btn');

    if (!list || items.length === 0) {
        return; // Don't initialize if essential parts are missing
    }

    let active = 0;
    let lengthItem = items.length - 1;

    // Create dots for pagination
    items.forEach(() => {
        const li = document.createElement('li');
        dotsContainer.appendChild(li);
    });
    const dots = dotsContainer.querySelectorAll('li');
    dots[0].classList.add('active');

    let refresh = setInterval(() => next.click(), 5000); // Auto-advance every 5 seconds

    function reloadSlider() {
        const checkleft = items[active].offsetLeft;
        list.style.left = -checkleft + 'px';

        const lastActiveDot = dotsContainer.querySelector('li.active');
        if (lastActiveDot) {
            lastActiveDot.classList.remove('active');
        }
        dots[active].classList.add('active');

        clearInterval(refresh);
        refresh = setInterval(() => next.click(), 5000);
    }

    next.onclick = () => {
        active = active >= lengthItem ? 0 : active + 1;
        reloadSlider();
    };

    prev.onclick = () => {
        active = active <= 0 ? lengthItem : active - 1;
        reloadSlider();
    };

    dots.forEach((dot, index) => {
        dot.onclick = () => {
            active = index;
            reloadSlider();
        };
    });
}

// When the page loads, find all sliders and initialize them
document.addEventListener('DOMContentLoaded', () => {
    // Initialize the main news slider
    const newsSlider = document.querySelector('.news:not(.recommendation-slider)');
    if (newsSlider) {
        initializeSlider(newsSlider);
    }

    // Initialize all recommendation sliders
    const recommendationSliders = document.querySelectorAll('.recommendation-slider');
    recommendationSliders.forEach(initializeSlider);
});