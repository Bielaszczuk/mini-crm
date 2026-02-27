document.addEventListener('DOMContentLoaded', () => {

    document.querySelectorAll('.alert').forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.4s';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 400);
        }, 4000);
    });

    const path = window.location.pathname;
    document.querySelectorAll('.nav-item').forEach(item => {
        const href = item.getAttribute('href');
        if (href && path.startsWith(href) && href !== '/') {
            item.classList.add('active');
        } else if (href === '/' && path === '/') {
            item.classList.add('active');
        }
    });

    document.querySelectorAll('form[data-confirm]').forEach(form => {
        form.addEventListener('submit', e => {
            if (!confirm(form.dataset.confirm)) e.preventDefault();
        });
    });
});
