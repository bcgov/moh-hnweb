<template>
  <table class="simple-table">
    <slot></slot>
  </table>
</template>

<style>
.simple-table {
  border-collapse: collapse;
  border-radius: 5px 5px 0 0;
  margin: 25px 0;
  min-width: 400px;
  overflow: hidden;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
}

.simple-table thead tr {
  background-color: #38598A;
  color: #ffffff;
  text-align: left;
}

.simple-table th,
.simple-table td {
  padding: 12px 15px;
}

.simple-table tbody tr {
  border-bottom: 1px solid #dddddd;
}

.simple-table tbody tr:nth-of-type(even) {
  background-color: #f3f3f3;
}

.simple-table tbody tr:last-of-type {
  border-bottom: 2px solid #38598A;
}
</style>
