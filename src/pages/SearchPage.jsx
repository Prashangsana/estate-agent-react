import { useState, useMemo } from "react";
import SearchForm from "../components/SearchForm";
import PropertyCard from "../components/PropertyCard";
import FavoritesList from "../components/FavoritesList";
import rawData from "../data/properties.json";
import PropTypes from "prop-types";

const SearchPage = ({
  favorites,
  onAddFavorite,
  onRemoveFavorite,
  onClearFavorites,
}) => {
  const allProperties = useMemo(() => {
    return rawData.properties.map(prop => ({
      ...prop,
      dateObject: new Date(`${prop.added.month} ${prop.added.day}, ${prop.added.year}`)
    }));
  }, []);

  const [filteredProps, setFilteredProps] = useState(allProperties);

  // Search Functionality
  const handleSearch = (criteria) => {
    const results = allProperties.filter((prop) => {
      // Type & Price
      if (criteria.type !== "any" && prop.type !== criteria.type) return false;
      if (criteria.minPrice && prop.price < Number(criteria.minPrice)) return false;
      if (criteria.maxPrice && prop.price > Number(criteria.maxPrice)) return false;
      
      // Beds
      if (criteria.minBed && prop.bedrooms < Number(criteria.minBed)) return false;
      if (criteria.maxBed && prop.bedrooms > Number(criteria.maxBed)) return false;

      // Postcode
      if (criteria.postcode && !prop.location.toLowerCase().includes(criteria.postcode.toLowerCase())) return false;

      // Date
      if (criteria.dateExact) {
        if (prop.dateObject.toDateString() !== criteria.dateExact.toDateString()) return false;
      } else {
        if (criteria.dateFrom && prop.dateObject < criteria.dateFrom) return false;
        if (criteria.dateTo && prop.dateObject > criteria.dateTo) return false;
      }

      return true;
    });
    setFilteredProps(results);
  };

  const handleToggleFavorite = (property) => {
    const isAlreadyFav = favorites.some((fav) => fav.id === property.id);
    isAlreadyFav ? onRemoveFavorite(property.id) : onAddFavorite(property);
  };

  // Drag Handlers
  const handlePropDragStart = (e, property) => {
    e.dataTransfer.setData("type", "ADD_FAV");
    e.dataTransfer.setData("payload", JSON.stringify(property));
  };

  const handleSidebarDrop = (e) => {
    e.preventDefault();
    if (e.dataTransfer.getData("type") === "ADD_FAV") {
      onAddFavorite(JSON.parse(e.dataTransfer.getData("payload")));
    }
  };

  const handleMainDrop = (e) => {
    e.preventDefault();
    if (e.dataTransfer.getData("type") === "REMOVE_FAV") {
      onRemoveFavorite(e.dataTransfer.getData("payload"));
    }
  };

  const handleDragOver = (e) => e.preventDefault();

  return (
    <div className="page-layout page-transition">
      {/* Sidebar */}
      <aside
        className="sidebar"
        onDrop={handleSidebarDrop}
        onDragOver={handleDragOver}
      >
        <FavoritesList
          favorites={favorites}
          onRemove={onRemoveFavorite}
          onClear={onClearFavorites}
          onDragStart={(e, favId) => {
            e.dataTransfer.setData("type", "REMOVE_FAV");
            e.dataTransfer.setData("payload", favId);
          }}
        />
      </aside>

      {/* Main Content */}
      <section
        className="content"
        onDrop={handleMainDrop}
        onDragOver={handleDragOver}
      >
        <SearchForm onSearch={handleSearch} />

        <div className="section-header">
          <h2>Explore our featured properties</h2>
          <p>
            Take a look at our latest listings. From cozy city apartments to spacious family homes.
          </p>
        </div>

        <div className="results-grid">
          {filteredProps.map((prop) => (
            <div
              key={prop.id}
              draggable="true"
              onDragStart={(e) => handlePropDragStart(e, prop)}
            >
              <PropertyCard
                property={prop}
                isFavorite={favorites.some((f) => f.id === prop.id)}
                onToggleFavorite={handleToggleFavorite}
              />
            </div>
          ))}
          {filteredProps.length === 0 && (
            <p className="no-results">No properties found.</p>
          )}
        </div>
      </section>
    </div>
  );
};

SearchPage.propTypes = {
  favorites: PropTypes.array.isRequired,
  onAddFavorite: PropTypes.func.isRequired,
  onRemoveFavorite: PropTypes.func.isRequired,
  onClearFavorites: PropTypes.func.isRequired,
};

export default SearchPage;