import "./SearchForm.css";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { useState } from "react";
import PropTypes from "prop-types";

const SearchForm = ({ onSearch }) => {
  const initialCriteria = {
    type: "any",
    minPrice: "",
    maxPrice: "",
    minBed: "",
    maxBed: "",
    postcode: "",
    dateFrom: null,
    dateTo: null,
    dateExact: null,
  };

  const [criteria, setCriteria] = useState(initialCriteria);

  const updateAndSearch = (updatedCriteria) => {
    setCriteria(updatedCriteria);
    onSearch(updatedCriteria);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;

    // Validate non-negative numbers
    if (["minPrice", "maxPrice", "minBed", "maxBed"].includes(name)) {
      if (value < 0) return;
    }

    let finalValue = value;
    if (
      ["minPrice", "maxPrice", "minBed", "maxBed"].includes(name) &&
      value === "0"
    ) {
      finalValue = "";
    }

    const updated = { ...criteria, [name]: finalValue };
    updateAndSearch(updated);
  };

  const handleDateChange = (field, date) => {
    let updated = { ...criteria, [field]: date };

    if (field === "dateExact") {
      updated.dateFrom = null;
      updated.dateTo = null;
    } else {
      updated.dateExact = null;
    }
    updateAndSearch(updated);
  };

  const handleClear = () => {
    updateAndSearch(initialCriteria);
  };

  return (
    <div className="search-container" aria-label="Search Filters">
      {/* Row 1 */}

      {/* Col 1: Type */}
      <div className="filter-group">
        <label>Property Type</label>
        <select
          name="type"
          value={criteria.type}
          onChange={handleChange}
          className="form-control"
        >
          <option value="any">Any</option>
          <option value="House">House</option>
          <option value="Flat">Flat</option>
        </select>
      </div>

      {/* Col 2: Price */}
      <div className="filter-group">
        <label>Price ($)</label>
        <div className="range-inputs">
          <input
            type="number"
            name="minPrice"
            value={criteria.minPrice}
            placeholder="Min"
            onChange={handleChange}
            min="0"
            step="1000"
          />
          <input
            type="number"
            name="maxPrice"
            value={criteria.maxPrice}
            placeholder="Max"
            onChange={handleChange}
            min="0"
            step="1000"
          />
        </div>
      </div>

      {/* Col 3: Bedrooms */}
      <div className="filter-group">
        <label>Bedrooms</label>
        <div className="range-inputs">
          <input
            type="number"
            name="minBed"
            value={criteria.minBed}
            placeholder="Min"
            onChange={handleChange}
            min="0"
            step="1"
          />
          <input
            type="number"
            name="maxBed"
            value={criteria.maxBed}
            placeholder="Max"
            onChange={handleChange}
            min="0"
            step="1"
          />
        </div>
      </div>

      {/* Row 2 */}

      {/* Col 1: Postcode */}
      <div className="filter-group">
        <label>Postcode</label>
        <input
          type="text"
          name="postcode"
          value={criteria.postcode}
          placeholder="Area (e.g. BR1)"
          onChange={handleChange}
        />
      </div>

      {/* Col 2: Date Exact */}
      <div className="filter-group">
        <label>Date Added</label>
        <DatePicker
          selected={criteria.dateExact}
          onChange={(date) => handleDateChange("dateExact", date)}
          placeholderText="Select Date"
          className="date-input"
        />
      </div>

      {/* Col 3: Date Range */}
      <div className="filter-group">
        <label>Date Range</label>
        <div className="range-inputs">
          <DatePicker
            selected={criteria.dateFrom}
            onChange={(date) => handleDateChange("dateFrom", date)}
            placeholderText="From"
            className="date-input"
          />
          <DatePicker
            selected={criteria.dateTo}
            onChange={(date) => handleDateChange("dateTo", date)}
            placeholderText="To"
            className="date-input"
          />
        </div>
      </div>

      {/* Row 3 */}
      <div className="button-row">
        <button
          type="button"
          onClick={handleClear}
          className="clear-btn-final"
        >
          Clear Filters
        </button>
      </div>
    </div>
  );
};

SearchForm.propTypes = {
  onSearch: PropTypes.func.isRequired,
};

export default SearchForm;