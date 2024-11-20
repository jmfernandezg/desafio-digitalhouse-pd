const getPageNumbers = (currentPage, totalPages) => {
    const delta = 2; // Number of pages to show before and after current page
    const pages = [];

    // Always show first page
    pages.push(1);

    // Calculate range around current page
    let rangeStart = Math.max(2, currentPage - delta);
    let rangeEnd = Math.min(totalPages - 1, currentPage + delta);

    // Adjust range if current page is near start or end
    if (currentPage - delta <= 2) {
        rangeEnd = Math.min(totalPages - 1, 1 + 2 * delta);
    }
    if (currentPage + delta >= totalPages - 1) {
        rangeStart = Math.max(2, totalPages - 2 * delta);
    }

    // Add ellipsis before range if needed
    if (rangeStart > 2) {
        pages.push('...');
    }

    // Add range pages
    for (let i = rangeStart; i <= rangeEnd; i++) {
        pages.push(i);
    }

    // Add ellipsis after range if needed
    if (rangeEnd < totalPages - 1) {
        pages.push('...');
    }

    // Always show last page if there is more than one page
    if (totalPages > 1) {
        pages.push(totalPages);
    }

    return pages;
};

