import "./NavBar.css"; 
import { Link } from "react-router-dom";
import { useState } from "react";
import { FcGoogle } from "react-icons/fc";
import { FaTimes } from "react-icons/fa";

const NavBar = () => {
  const [showModal, setShowModal] = useState(false);

  const handleOpen = () => setShowModal(true);
  const handleClose = () => setShowModal(false);

  return (
    <>
      <nav>
        <Link to="/" className="nav-logo">
          Estate Agent
        </Link>

        <button className="signup-trigger-btn" onClick={handleOpen}>
          Sign Up
        </button>
      </nav>

      {showModal && (
        <div className="modal-overlay" onClick={handleClose}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <button className="modal-close-btn" onClick={handleClose}>
              <FaTimes />
            </button>

            <h2 className="modal-title">Sign Up to Estate Agent</h2>

            <form onSubmit={(e) => e.preventDefault()}>
              <label className="modal-label">Email address</label>
              <input 
                type="email" 
                placeholder="name@domain.com" 
                className="form-control"
                autoFocus
              />

              <button type="submit" className="continue-btn">
                Continue
              </button>
            </form>

            <div className="divider">or</div>

            <button className="google-btn">
              <FcGoogle style={{ fontSize: "1.2rem" }} />
              Sign up with Google
            </button>

            <div className="login-section">
              Already have an account? 
              <button className="login-link" onClick={() => console.log("Go to Login")}>
                Log in
              </button>
            </div>

            <p className="legal-text">
              By clicking on Sign up, you agree to Estate Agent's <a href="#" className="legal-link">Terms and Conditions of Use</a>.
            </p>
            <p className="legal-text">
              To learn more about how Estate Agent collects, uses, shares and protects your personal data please read Estate Agent's <a href="#" className="legal-link">Privacy Policy</a>.
            </p>

            
          </div>
        </div>
      )}
    </>
  );
};

export default NavBar;