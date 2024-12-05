import React from "react";
import { useTable } from "react-table";

const ProductoMasVendidos = () => {
  const columns = React.useMemo(() => [
    { Header: "Producto", accessor: "name" },
    { Header: "Cantidad Vendida", accessor: "quantity" },
  ], []);

  const data = React.useMemo(() => [
    { name: "Producto A", quantity: 50 },
    { name: "Producto B", quantity: 30 },
  ], []);

  const tableInstance = useTable({ columns, data });

  const { getTableProps, getTableBodyProps, headerGroups, rows, prepareRow } = tableInstance;

  return (
    <table {...getTableProps()} style={{ border: "1px solid black" }}>
      <thead>
        {headerGroups.map((headerGroup) => (
          <tr {...headerGroup.getHeaderGroupProps()}>
            {headerGroup.headers.map((column) => (
              <th {...column.getHeaderProps()}>{column.render("Header")}</th>
            ))}
          </tr>
        ))}
      </thead>
      <tbody {...getTableBodyProps()}>
        {rows.map((row) => {
          prepareRow(row);
          return (
            <tr {...row.getRowProps()}>
              {row.cells.map((cell) => (
                <td {...cell.getCellProps()}>{cell.render("Cell")}</td>
              ))}
            </tr>
          );
        })}
      </tbody>
    </table>
  );
};

export default ProductoMasVendidos;
