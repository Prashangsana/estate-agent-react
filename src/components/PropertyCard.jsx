import "./PropertyCard.css";
import { Link } from "react-router-dom";
import PropTypes from "prop-types";
import { FaHeart, FaRegHeart } from "react-icons/fa";

const PropertyCard = ({ property, isFavorite, onToggleFavorite }) => {
  const thumbnail =
    property.images && property.images.length > 0
      ? `/assets/${property.images[0]}`
      : "https://placehold.co/600x200";

  return (
    <div className="property-card">
      <div className="card-image-container">
        <img
          src={thumbnail}
          alt={property.type}
          onError={(e) => (e.target.src = "https://placehold.co/600x200")}
        />

        {/* Favorite Button Overlay */}
        <button
          className="fav-btn"
          onClick={(e) => {
            e.preventDefault();
            onToggleFavorite(property);
          }}
          aria-label={isFavorite ? "Remove from favorites" : "Add to favorites"}
        >
          {isFavorite ? (
            <FaHeart className="heart-icon filled" />
          ) : (
            <FaRegHeart className="heart-icon" />
          )}
        </button>
      </div>

      <div className="card-info">
        <h3>{property.location}</h3>
        <p className="price">${property.price.toLocaleString()}</p>
        <div className="details">
          <span>{property.type}</span> • <span>{property.bedrooms} Bed</span>
        </div>

        <p className="description">{property.shortDescription}</p>

        <Link to={`/property/${property.id}`} className="view-btn">
          View Details
        </Link>
      </div>
    </div>
  );
};

PropertyCard.propTypes = {
  property: PropTypes.object.isRequired,
  isFavorite: PropTypes.bool,
  onToggleFavorite: PropTypes.func,
};

export default PropertyCard;