
const endpoints = {
  resources: "/ui-api/resources/api/reservoirs",
  quality: "/ui-api/quality/api/quality",
  complaints: "/ui-api/complaints/api/complaints",
  pumps: "/ui-api/infrastructure/api/pumps",
  pipelines: "/ui-api/infrastructure/api/pipelines",
  maintenance: "/ui-api/infrastructure/api/maintenance"
};

async function api(url, options = {}) {
  const response = await fetch(url, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {})
    }
  });

  if (!response.ok) {
    throw new Error(`Request failed: ${response.status} ${await response.text()}`);
  }

  if (response.status === 204) return null;
  const text = await response.text();
  return text ? JSON.parse(text) : null;
}

function showError(error) {
  alert(error.message || "Something went wrong. Check whether the services are running.");
}

function makeTable(container, records) {
  container.replaceChildren();

  if (!Array.isArray(records) || records.length === 0) {
    container.textContent = "No records found.";
    return;
  }

  const table = document.createElement("table");
  table.border = "1";
  const keys = Object.keys(records[0]);
  const head = table.createTHead().insertRow();

  keys.forEach(key => {
    const cell = document.createElement("th");
    cell.textContent = key;
    head.appendChild(cell);
  });

  const body = table.createTBody();
  records.forEach(record => {
    const row = body.insertRow();
    keys.forEach(key => {
      row.insertCell().textContent = record[key] ?? "";
    });
  });

  container.appendChild(table);
}

async function loadTable(endpoint, elementId) {
  try {
    const records = await api(endpoint);
    makeTable(document.getElementById(elementId), records);
  } catch (error) {
    showError(error);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  const module = document.body.dataset.module;

  if (module === "resources") {
    loadTable(endpoints.resources, "records");
  }

  if (module === "quality") {
    loadTable(endpoints.quality, "records");
  }

  if (module === "complaints") {
    loadTable(endpoints.complaints, "records");
  }

  if (module === "infrastructure") {
    const selector = document.getElementById("kind");
    const refresh = () => loadTable(endpoints[selector.value], "records");
    selector.addEventListener("change", refresh);
    refresh();
  }

  document.querySelectorAll("form[data-endpoint]").forEach(form => {
    form.addEventListener("submit", async event => {
      event.preventDefault();

      const data = Object.fromEntries(new FormData(form).entries());

      for (const [key, value] of Object.entries(data)) {
        if (value.trim() !== "" && !Number.isNaN(Number(value))) {
          data[key] = Number(value);
        }
      }

      try {
        await api(form.dataset.endpoint, {
          method: "POST",
          body: JSON.stringify(data)
        });

        alert("Saved successfully.");
        form.reset();

        if (module === "resources") loadTable(endpoints.resources, "records");
        if (module === "quality") loadTable(endpoints.quality, "records");
        if (module === "complaints") loadTable(endpoints.complaints, "records");
      } catch (error) {
        showError(error);
      }
    });
  });
});
