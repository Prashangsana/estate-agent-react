import "./PropertyPage.css";
import { useParams } from "react-router-dom";
import { Tab, Tabs, TabList, TabPanel } from "react-tabs";
import "react-tabs/style/react-tabs.css";
import { useState, useEffect } from "react";
import data from "../data/properties.json";
import {
  FaHeart,
  FaRegHeart,
  FaChevronLeft,
  FaChevronRight,
} from "react-icons/fa";
import PropTypes from "prop-types";

const PropertyPage = ({ onAddFavorite, onRemoveFavorite, favorites }) => {
  const { id } = useParams();
  const property = data.properties.find((p) => p.id === id);

  const images = property?.images || [];
  const hasMultipleImages = images.length > 1;

  const [currentIndex, setCurrentIndex] = useState(0);

  useEffect(() => {
    window.scrollTo({ top: 0, behavior: "instant" });
  }, []);

  useEffect(() => {
    if (!hasMultipleImages) return;
    
    const interval = setInterval(() => {
      setCurrentIndex((prevIndex) => (prevIndex + 1) % images.length);
    }, 5000);
    
    return () => clearInterval(interval);
  }, [hasMultipleImages, images.length]);

  const isFav = favorites.some((f) => f.id === property?.id);

  const handleToggleFavorite = () => {
    if (isFav) {
      onRemoveFavorite(property.id);
    } else {
      onAddFavorite(property);
    }
  };

  const goToPrevious = (e) => {
    e.stopPropagation();
    setCurrentIndex((prev) => (prev === 0 ? images.length - 1 : prev - 1));
  };

  const goToNext = (e) => {
    e.stopPropagation();
    setCurrentIndex((prev) => (prev + 1) % images.length);
  };

  if (!property) return <div className="container">Property not found</div>;

  return (
    <div className="property-page container page-transition">
      <header className="prop-header">
        <div>
          <h1>{property.location}</h1>
          <h2 className="prop-price">${property.price.toLocaleString()}</h2>
        </div>

        <button
          className={`fav-action-btn ${isFav ? "active" : ""}`}
          onClick={handleToggleFavorite}
          aria-label={isFav ? "Remove from favorites" : "Add to favorites"}
        >
          {isFav ? <FaHeart /> : <FaRegHeart />}
        </button>
      </header>

      <div className="gallery">
        <div className="main-frame">
          {images.length > 0 ? (
            images.map((img, idx) => (
              <img
                key={idx}
                src={`/assets/${img}`}
                alt={`View ${idx + 1}`}
                className={`gallery-main-img ${idx === currentIndex ? "active" : ""}`}
                onError={(e) => (e.target.src = "https://placehold.co/800x500")}
              />
            ))
          ) : (
            <img 
              src="https://placehold.co/800x500?text=No+Images" 
              alt="Placeholder" 
              className="gallery-main-img active" 
            />
          )}

          {hasMultipleImages && (
            <>
              <button className="nav-arrow left-arrow" onClick={goToPrevious}>
                <FaChevronLeft />
              </button>
              <button className="nav-arrow right-arrow" onClick={goToNext}>
                <FaChevronRight />
              </button>
            </>
          )}
        </div>

        <div className="thumbnail-strip">
          {images.map((img, idx) => (
            <img
              key={idx}
              src={`/assets/${img}`}
              onClick={() => setCurrentIndex(idx)}
              alt={`Thumbnail ${idx + 1}`}
              className={currentIndex === idx ? "selected" : ""}
              onError={(e) => (e.target.src = "https://placehold.co/100x80")}
            />
          ))}
        </div>
      </div>

      <Tabs className="prop-tabs">
        <TabList>
          <Tab>Description</Tab>
          <Tab>Floor Plan</Tab>
          <Tab>Google Map</Tab>
        </TabList>

        <TabPanel>
          <div className="tab-content">
            <h3>Details</h3>
            <p className="prop-desc-text">{property.description}</p>
            <div className="features-grid">
              {[
                { label: "Property Type", value: property.type },
                { label: "Bedrooms", value: property.bedrooms },
                { label: "Tenure", value: property.tenure },
                { label: "Date Added", value: `${property.added.day} ${property.added.month} ${property.added.year}` },
              ].map((item, index) => (
                <div key={index} className="feature-item">
                  <span className="feature-label">{item.label}</span>
                  <span className="feature-value">{item.value}</span>
                </div>
              ))}
            </div>
          </div>
        </TabPanel>

        <TabPanel>
          <div className="tab-content floorplan-container">
            <h3>Floor Plan</h3>
            <img
              src={`/assets/${property.id}/floorplan.png`}
              alt="Floor Plan"
              className="floorplan-img"
              onError={(e) => (e.target.src = "https://placehold.co/600x400?text=No+Floor+Plan")}
            />
          </div>
        </TabPanel>

        <TabPanel>
          <div className="tab-content map-wrapper">
            <iframe
              title="Property Map"
              width="100%"
              height="500"
              style={{ border: 0 }}
              loading="lazy"
              allowFullScreen
              referrerPolicy="no-referrer-when-downgrade"
              src={`https://maps.google.com/maps?q=${encodeURIComponent(property.location)}&output=embed`}
            ></iframe>
          </div>
        </TabPanel>
      </Tabs>
    </div>
  );
};

PropertyPage.propTypes = {
  onAddFavorite: PropTypes.func,
  onRemoveFavorite: PropTypes.func,
  favorites: PropTypes.array,
};

export default PropertyPage;