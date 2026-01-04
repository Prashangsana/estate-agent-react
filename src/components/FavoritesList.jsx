import "./FavoritesList.css";
import { Link } from "react-router-dom";
import { FaTimes } from "react-icons/fa";
import PropTypes from "prop-types";

const FavoritesList = ({ favorites, onRemove, onClear, onDragStart }) => {
  return (
    <div className="favorites-box">
      <div className="fav-header">
        <h3>Favourite Properties</h3>
        {favorites.length > 0 && (
          <span className="fav-count-badge">{favorites.length}</span>
        )}
      </div>
      
      <hr />

      {favorites.length === 0 ? (
        <div className="empty-state">
          <p className="main-text">No favourites yet</p>
          <p className="sub-text">
            Drag properties here or click the Star icon to add them to your
            favourites
          </p>
        </div>
      ) : (
        <>
          <ul className="fav-list">
            {favorites.map((fav) => {
              const thumbnail = fav.images && fav.images.length > 0
                ? `/assets/${fav.images[0]}`
                : "https://placehold.co/100x100";

              return (
                <li
                  key={fav.id}
                  className="fav-item"
                  draggable="true"
                  onDragStart={(e) => onDragStart(e, fav.id)}
                >
                  <Link to={`/property/${fav.id}`} className="fav-link-wrapper">
                    
                    {/* The Image Thumbnail */}
                    <img 
                      src={thumbnail} 
                      alt="property" 
                      className="fav-item-img"
                      onError={(e) => (e.target.src = "https://placehold.co/100x100")} 
                    />

                    {/* The Text Info */}
                    <div className="fav-info">
                      <span className="fav-loc">
                        {fav.location.split(",")[0]}
                      </span>
                      <span className="fav-price">
                        ${fav.price.toLocaleString()}
                      </span>
                    </div>
                  </Link>

                  {/* The Close Button */}
                  <button
                    onClick={(e) => {
                      e.preventDefault();
                      onRemove(fav.id);
                    }}
                    className="remove-btn"
                    aria-label="Remove property"
                  >
                    <FaTimes />
                  </button>
                </li>
              );
            })}
          </ul>
          <button onClick={onClear} className="clear-btn">
            Clear Favorites
          </button>
        </>
      )}
    </div>
  );
};

FavoritesList.propTypes = {
  favorites: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      location: PropTypes.string.isRequired,
      price: PropTypes.number.isRequired,
    })
  ).isRequired,
  onRemove: PropTypes.func.isRequired,
  onClear: PropTypes.func.isRequired,
  onDragStart: PropTypes.func.isRequired,
};

export default FavoritesList;