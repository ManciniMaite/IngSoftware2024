import React, { createContext, useContext, useState } from 'react';

// Creamos el contexto
const SidebarContext = createContext();

// Hook para usar el contexto
export const useSidebar = () => useContext(SidebarContext);

// Componente proveedor para envolver la aplicación
export const SidebarProvider = ({ children }) => {
  const [isSidebarVisible, setIsSidebarVisible] = useState(false);

  const toggleSidebar = () => {
    setIsSidebarVisible(!isSidebarVisible);
  };

  return (
    <SidebarContext.Provider value={{ isSidebarVisible, toggleSidebar }}>
      {children}
    </SidebarContext.Provider>
  );
};
