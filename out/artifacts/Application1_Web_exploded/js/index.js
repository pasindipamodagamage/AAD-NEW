document.addEventListener("DOMContentLoaded", function () {
  const sections = document.querySelectorAll("section");

  // Hide all sections
  function hideAllSections() {
    sections.forEach((section) => {
      section.style.display = "none";
    });
  }

  // Show specific section
  function showSection(targetSectionId) {
    hideAllSections();
    const targetSection = document.querySelector(targetSectionId);
    if (targetSection) {
      targetSection.style.display = "block";
    }
  }

  // Load last visited section or default to #customer
  const lastVisited = localStorage.getItem("lastVisitedSection") || "#customer";
  showSection(lastVisited);

  // Add click events to navigation links
  document.querySelectorAll(".nav-link").forEach((link) => {
    link.addEventListener("click", function (event) {
      event.preventDefault();

      const targetSectionId = this.getAttribute("href");

      // Show the selected section
      showSection(targetSectionId);

      // Save the last visited section in local storage
      localStorage.setItem("lastVisitedSection", targetSectionId);

      // Update document title based on section
      switch (targetSectionId) {
        case "#customer":
          document.title = "Customer Form";
          break;
        case "#item":
          document.title = "Item Form";
          break;
        default:
          document.title = "Form";
          break;
      }
    });
  });
});

//Customer Form Functions
// Attach click event listener to the table body rows
$("#customer_tbody").on("click", "tr", function () {
  const id = $(this).find("td:nth-child(1)").text();
  const name = $(this).find("td:nth-child(2)").text();
  const address = $(this).find("td:nth-child(3)").text();

  $("#ID").val(id);
  $("#Name").val(name);
  $("#Address").val(address);
});

//get all customers
const fetchCustomerData = () => {
  $.ajax({
    url: "http://localhost:8080/Application1_Web_exploded/customer",
    type: "GET",
    success: (res) => {
      $("#customer_tbody").empty();
      res.forEach((customer) => {
        $("#customer_tbody").append(
          <tr>
            <td>${customer.id}</td>
            <td>${customer.name}</td>
            <td>${customer.address}</td>
          </tr>
        );
      });
    },
    error: (error) => {
      console.log(error);
    },
  });
};

//load data
$("#btn_load").click((e) => {
  e.preventDefault();

  const id = $("#id").val();
  const name = $("#name").val();
  const address = $("#address").val();

  $.ajax({
    url: "http://localhost:8080/Application1_Web_exploded/customer",
    type: "GET",
    data: {
      name: id,
      email: name,
      age: address,
      success: () => {
        fetchCustomerData();
      },
    },
  });
});

//add data
$("#btn_add").click((e) => {
  e.preventDefault();

  const id = $("#ID").val();
  const name = $("#Name").val();
  const address = $("#Address").val();

  $.ajax({
    url: "http://localhost:8080/Application1_Web_exploded/customer",
    type: "POST",
    data: { id, name, address },
    success: (res) => {
      fetchCustomerData();
      $("#ID").val("");
      $("#Name").val("");
      $("#Address").val("");
    },
    error: (error) => {
      console.log(error);
    },
  });
});

//update data
$("#btn_update").click((e) => {
  e.preventDefault();

  const id = $("#ID").val();
  const name = $("#Name").val();
  const address = $("#Address").val();

  $.ajax({
    url: `http://localhost:8080/Application1_Web_exploded/customer?id=${id}&name=${name}&address=${address}`,
    type: "PUT",
    data: { id, name, address },
    success: (res) => {
      console.log("updated");
      fetchCustomerData();
      $("#ID").val("");
      $("#Name").val("");
      $("#Address").val("");
    },
    error: (error) => {
      console.log(error);
    },
  });
});

//delete data
$("#btn_delete").click((e) => {
  e.preventDefault();

  const id = $("#ID").val();

  if (confirm("Are you sure you want to delete this customer?")) {
    $.ajax({
      url: `http://localhost:8080/Application1_Web_exploded/customer?id=${id}`,
      type: "DELETE",
      success: (res) => {
        alert("Customer deleted successfully...!");
        fetchCustomerData();
        $("#ID").val("");
        $("#Name").val("");
        $("#Address").val("");
      },
      error: (error) => {
        console.log(error);
      },
    });
  }
});

fetchCustomerData();

//Item Form Functions
// Attach click event listener to the table body rows
$("#customer_tbody").on("click", "tr", function () {
  const code = $(this).find("td:nth-child(1)").text();
  const name = $(this).find("td:nth-child(2)").text();
  const price = $(this).find("td:nth-child(3)").text();
  const qty = $(this).find("td:nth-child(4)").text();

  $("#itemCode").val(code);
  $("#itemName").val(name);
  $("#itemPrice").val(price);
  $("#itemQty").val(qty);
});

//get all items
const fetchItemData = () => {
  $.ajax({
    url: "http://localhost:8080/Application1_Web_exploded/item",
    type: "GET",
    success: (res) => {
      $("#item_tbody").empty();
      res.forEach((item) => {
        $("#item_tbody").append(
          <tr>
            <td>${item.code}</td>
            <td>${item.name}</td>
            <td>${item.price}</td>
            <td>${item.qty}</td>
          </tr>
        );
      });
    },
    error: (error) => {
      console.log(error);
    },
  });
};

// load data
$();
