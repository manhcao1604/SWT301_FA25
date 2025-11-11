// Admin JS
console.log('Admin JS loaded');

document.addEventListener('DOMContentLoaded', function() {
    // Data table functionality
    const dataTables = document.querySelectorAll('.data-table');

    dataTables.forEach(table => {
        const searchInput = table.querySelector('.table-search');
        if (searchInput) {
            searchInput.addEventListener('input', function() {
                const filter = this.value.toLowerCase();
                const rows = table.querySelectorAll('tbody tr');

                rows.forEach(row => {
                    const text = row.textContent.toLowerCase();
                    row.style.display = text.includes(filter) ? '' : 'none';
                });
            });
        }
    });

    // Confirmation for delete actions
    const deleteButtons = document.querySelectorAll('.btn-delete');

    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (!confirm('Are you sure you want to delete this item?')) {
                e.preventDefault();
            }
        });
    });

    // Form submission with loading state
    const adminForms = document.querySelectorAll('.admin-form');

    adminForms.forEach(form => {
        form.addEventListener('submit', function(e) {
            const submitButton = this.querySelector('button[type="submit"]');
            if (submitButton) {
                submitButton.disabled = true;
                submitButton.innerHTML = '<span class="loading-spinner"></span> Processing...';
            }
        });
    });

    // Image preview for file inputs
    const imageInputs = document.querySelectorAll('input[type="file"][accept="image/*"]');

    imageInputs.forEach(input => {
        input.addEventListener('change', function() {
            const preview = this.parentNode.querySelector('.image-preview');
            if (preview && this.files && this.files[0]) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    preview.innerHTML = `<img src="${e.target.result}" class="img-thumbnail" style="max-height: 200px;">`;
                };
                reader.readAsDataURL(this.files[0]);
            }
        });
    });

    // Statistics charts (placeholder for chart integration)
    function initCharts() {
        // This would integrate with a charting library like Chart.js
        console.log('Initialize charts here');
    }

    initCharts();
});

// Admin notification system
function showAdminNotification(message, type = 'success') {
    const notification = document.createElement('div');
    notification.className = `alert alert-${type} alert-dismissible fade show`;
    notification.style.cssText = 'position: fixed; top: 70px; right: 20px; z-index: 9999;';

    notification.innerHTML = `
        <i class="fas fa-${type === 'success' ? 'check' : 'exclamation-triangle'} me-2"></i>
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

    document.body.appendChild(notification);

    setTimeout(() => {
        if (notification.parentNode) {
            notification.remove();
        }
    }, 5000);
}

// Bulk actions
function handleBulkAction(action) {
    const selectedItems = document.querySelectorAll('input[name="selectedItems"]:checked');

    if (selectedItems.length === 0) {
        showAdminNotification('Please select at least one item', 'warning');
        return;
    }

    const itemIds = Array.from(selectedItems).map(item => item.value);

    if (action === 'delete') {
        if (!confirm(`Are you sure you want to delete ${selectedItems.length} item(s)?`)) {
            return;
        }
    }

    // Simulate API call
    console.log(`Performing ${action} on items:`, itemIds);
    showAdminNotification(`${action.charAt(0).toUpperCase() + action.slice(1)} action completed successfully`);
}