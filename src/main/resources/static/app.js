const apiUrl = "/api/tasks";
const form = document.querySelector("#task-form");
const message = document.querySelector("#message");
const taskList = document.querySelector("#task-list");
const filter = document.querySelector("#status-filter");
const cancelButton = document.querySelector("#cancel-button");
let editingId = null;

// One fetch helper gives all UI actions the same API error behavior.
async function request(url, options = {}) {
    const response = await fetch(url, options);
    if (!response.ok) {
        const error = await response.json().catch(() => ({ message: "Something went wrong" }));
        throw new Error(error.message || "Something went wrong");
    }
    return response.status === 204 ? null : response.json();
}

function showMessage(text, isError = false) {
    message.textContent = text;
    message.className = `message ${isError ? "error" : "success"}`;
}

function formatStatus(status) { return status.replace("_", " ").toLowerCase().replace(/^./, c => c.toUpperCase()); }
function formatPriority(priority) { return priority.toLowerCase().replace(/^./, c => c.toUpperCase()); }

function renderTasks(tasks) {
    taskList.innerHTML = "";
    document.querySelector("#task-count").textContent = `${tasks.length} ${tasks.length === 1 ? "task" : "tasks"}`;
    if (!tasks.length) {
        taskList.innerHTML = '<div class="empty-state"><strong>No tasks found</strong><br>Create one or change the filter.</div>';
        return;
    }
    const template = document.querySelector("#task-template");
    tasks.forEach(task => {
        const card = template.content.cloneNode(true);
        card.querySelector("h3").textContent = task.title;
        card.querySelector(".description").textContent = task.description || "No description provided.";
        card.querySelector(".priority").textContent = `${formatPriority(task.priority)} priority`;
        card.querySelector(".priority").classList.add(`priority-${task.priority.toLowerCase()}`);
        card.querySelector(".status").textContent = formatStatus(task.status);
        card.querySelector(".due-date").textContent = task.dueDate ? `Due ${new Date(`${task.dueDate}T00:00:00`).toLocaleDateString()}` : "No due date";
        card.querySelector(".edit-button").addEventListener("click", () => populateForm(task));
        card.querySelector(".delete-button").addEventListener("click", () => deleteTask(task));
        taskList.append(card);
    });
}

async function loadTasks() {
    try {
        const suffix = filter.value ? `?status=${filter.value}` : "";
        renderTasks(await request(`${apiUrl}${suffix}`));
    } catch (error) { showMessage(error.message, true); }
}

function populateForm(task) {
    editingId = task.id;
    form.title.value = task.title;
    form.description.value = task.description || "";
    form.priority.value = task.priority;
    form.status.value = task.status;
    form.dueDate.value = task.dueDate || "";
    document.querySelector("#form-title").textContent = "Edit task";
    cancelButton.hidden = false;
    form.title.focus();
}

function resetForm() {
    form.reset();
    form.priority.value = "MEDIUM";
    editingId = null;
    document.querySelector("#form-title").textContent = "Create a task";
    cancelButton.hidden = true;
}

form.addEventListener("submit", async event => {
    event.preventDefault();
    const task = { title: form.title.value, description: form.description.value || null, priority: form.priority.value, status: form.status.value, dueDate: form.dueDate.value || null };
    try {
        await request(editingId ? `${apiUrl}/${editingId}` : apiUrl, { method: editingId ? "PUT" : "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(task) });
        showMessage(editingId ? "Task updated." : "Task created.");
        resetForm();
        await loadTasks();
    } catch (error) { showMessage(error.message, true); }
});

async function deleteTask(task) {
    if (!confirm(`Delete "${task.title}"?`)) return;
    try { await request(`${apiUrl}/${task.id}`, { method: "DELETE" }); showMessage("Task deleted."); await loadTasks(); }
    catch (error) { showMessage(error.message, true); }
}

filter.addEventListener("change", loadTasks);
cancelButton.addEventListener("click", resetForm);
loadTasks();
