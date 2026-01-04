import { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import SearchPage from './pages/SearchPage';
import PropertyPage from './pages/PropertyPage';
import NavBar from './components/NavBar';
import Footer from './components/Footer';

function App() {
  const [favorites, setFavorites] = useState([]);

  const handleAddFavorite = (property) => {
    if (!favorites.some(fav => fav.id === property.id)) {
      setFavorites(prev => [...prev, property]);
    }
  };

  const handleRemoveFavorite = (id) => {
    setFavorites(prev => prev.filter(fav => fav.id !== id));
  };

  const handleClearFavorites = () => setFavorites([]);

  return (
    <Router>
      <div className="app-container">
        <NavBar />
        <main>
          <Routes>
            <Route 
              path="/" 
              element={
                <SearchPage 
                  favorites={favorites} 
                  onAddFavorite={handleAddFavorite}
                  onRemoveFavorite={handleRemoveFavorite}
                  onClearFavorites={handleClearFavorites}
                />
              } 
            />
            <Route 
              path="/property/:id" 
              element={
                <PropertyPage 
                  onAddFavorite={handleAddFavorite} 
                  onRemoveFavorite={handleRemoveFavorite}
                  favorites={favorites}
                />
              } 
            />
          </Routes>
        </main>
      </div>
      <Footer />
    </Router>
  );
}

export default App;