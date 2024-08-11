// context/ThemeProvider.jsx
import React, { createContext, useState, useEffect } from 'react';

// Create a context
export const ThemeContext = createContext();

// ThemeProvider component
export const ThemeProvider = ({ children }) => {
    // Retrieve initial theme from localStorage or default to light
    const [theme, setTheme] = useState(localStorage.getItem('theme') || 'light');

    useEffect(() => {
        // Apply the theme to the HTML element
        document.documentElement.setAttribute('data-bs-theme', theme);
        // Store the theme in localStorage
        localStorage.setItem('theme', theme);
    }, [theme]);

    // Function to toggle the theme
    const toggleTheme = (newTheme) => {
        setTheme(newTheme);
    };

    return (
        <ThemeContext.Provider value={{ theme, toggleTheme }}>
            {children}
        </ThemeContext.Provider>
    );
};

// Custom hook for easy access to the theme context
export const useTheme = () => React.useContext(ThemeContext);
