function printReport() {
    const title = createTitle();
    const table = createReportTable();
    $(combineElements(title, table)).printThis();

}

function createTitle() {
    const title = document.createElement('h1');
    title.classList.add('text-center');
    title.innerText = REPORT_TITLE;
    return title;
}

function createReportTable() {
    const table = document.getElementById('report-table');

    const newTable = document.createElement('table');
    newTable.className = table.className; // copy the classes from the original table

    newTable.classList.add('table', 'table-striped', 'table-hover');

    const originalHeader = table.querySelector('thead');
    if (originalHeader) {
        const newHeader = document.createElement('thead');
        const headerRow = document.createElement('tr');

        for (let i = 0; i < originalHeader.rows[0].cells.length; i++) {
            const cell = originalHeader.rows[0].cells[i];

            const newCell = document.createElement('th');
            newCell.innerText = cell.innerText;
            headerRow.appendChild(newCell);
        }

        newHeader.appendChild(headerRow);
        newTable.appendChild(newHeader);
    }

    const originalBody = table.querySelector('tbody');
    if (originalBody) {
        const newBody = document.createElement('tbody');

        for (let i = 0; i < originalBody.rows.length; i++) {
            const newRow = document.createElement('tr');

            for (let j = 0; j < originalBody.rows[i].cells.length; j++) {
                const cell = originalBody.rows[i].cells[j];

                if (!cell.classList.contains('not-printable')) {
                    const newCell = document.createElement('td');
                    newCell.innerText = cell.innerText;
                    newRow.appendChild(newCell);
                }
            }

            newBody.appendChild(newRow);
        }

        newTable.appendChild(newBody);
    }

    return newTable;
}

function combineElements(el1, el2) {
    const div = document.createElement('div');
    div.appendChild(el1);
    div.appendChild(el2);
    return div;
}