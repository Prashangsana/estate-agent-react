import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import SearchForm from './components/SearchForm';
import PropertyCard from './components/PropertyCard';

// Mock property data
const mockProperty = {
  id: "prop1",
  type: "House",
  bedrooms: 3,
  price: 750000,
  location: "Test Location, London",
  shortDescription: "A lovely test home.",
  images: ["test-image.jpg"]
};

describe('Real Estate App Tests', () => {

  // TEST 1 COMPONENT RENDERING
  // Ensures the SearchForm loads with the correct inputs visible to the user
  test('1. SearchForm renders key filter inputs correctly', () => {
    render(<SearchForm onSearch={() => {}} />);
    
    // Check for "Property Type" dropdown
    expect(screen.getByText(/Property Type/i)).toBeInTheDocument();
    
    // Check for "Postcode" input by its placeholder
    expect(screen.getByPlaceholderText(/Area \(e.g. BR1\)/i)).toBeInTheDocument();
    
    // Check for the "Clear Filters" button
    expect(screen.getByText(/Clear Filters/i)).toBeInTheDocument();
  });

  // TEST 2 INTERACTION (FILTERING)
  // Ensures that typing in a box actually triggers the search function
  test('2. Typing in Postcode triggers the onSearch function', () => {
    const mockOnSearch = jest.fn(); // Create a fake function to track calls
    render(<SearchForm onSearch={mockOnSearch} />);
    
    const postcodeInput = screen.getByPlaceholderText(/Area \(e.g. BR1\)/i);
    
    // Simulate user typing "BR5"
    fireEvent.change(postcodeInput, { target: { value: 'BR5' } });
    
    // Expect our fake function to have been called
    expect(mockOnSearch).toHaveBeenCalled();
  });

  // TEST 3 INTERACTION (RESET)
  // Ensures the "Clear Filters" button works
  test('3. Clicking Clear Filters resets search criteria', () => {
    const mockOnSearch = jest.fn();
    render(<SearchForm onSearch={mockOnSearch} />);
    
    const clearBtn = screen.getByText(/Clear Filters/i);
    fireEvent.click(clearBtn);
    
    // It should trigger a search with the default empty values
    expect(mockOnSearch).toHaveBeenCalled();
  });

  // TEST 4 DATA DISPLAY
  // Ensures the Card component actually shows the data we pass to it
  test('4. PropertyCard displays correct price and location', () => {
    // Wrap in MemoryRouter because PropertyCard uses <Link>
    render(
      <MemoryRouter>
        <PropertyCard property={mockProperty} />
      </MemoryRouter>
    );
    
    expect(screen.getByText('Test Location, London')).toBeInTheDocument();
    expect(screen.getByText('$750,000')).toBeInTheDocument();
    expect(screen.getByText('3 Bed')).toBeInTheDocument();
  });

  // TEST 5 LOGIC (FAVORITES)
  // Ensures the heart button works and sends the correct data back
  test('5. Clicking Favorite button calls toggle function with property data', () => {
    const mockToggle = jest.fn();
    render(
      <MemoryRouter>
        <PropertyCard 
          property={mockProperty} 
          isFavorite={false} 
          onToggleFavorite={mockToggle} 
        />
      </MemoryRouter>
    );
    
    // Find button by aria-label
    const favBtn = screen.getByLabelText(/Add to favorites/i);
    fireEvent.click(favBtn);
    
    // Verify the function was called with the specific property object
    expect(mockToggle).toHaveBeenCalledWith(mockProperty);
  });

});