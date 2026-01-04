import "./Footer.css";
import { FaFacebook, FaTwitter, FaInstagram, FaLinkedin, FaPhone, FaEnvelope, FaMapMarkerAlt } from "react-icons/fa";
import { Link } from "react-router-dom";

const Footer = () => {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="site-footer">
      <div className="container footer-content">
        
        {/* Brand Info */}
        <div className="footer-col brand-col">
          <h3>Estate Agent</h3>
          <p>Your trusted partner in real estate since 2020.</p>
        </div>

        {/* Quick Links */}
        <div className="footer-col">
          <h4>Quick Links</h4>
          <ul>
            <li><Link to="/">Properties</Link></li>
            <li><Link to="/">About Us</Link></li>
            <li><Link to="/">Contact</Link></li>
            <li><Link to="/">Services</Link></li>
          </ul>
        </div>

        {/* Contact Us */}
        <div className="footer-col">
          <h4>Contact Us</h4>
          <ul className="contact-list">
            <li>
              <FaPhone className="footer-icon" />
              <a href="tel:+442071234567">+44 20 7123 4567</a>
            </li>
            <li>
              <FaEnvelope className="footer-icon" />
              <a href="mailto:info@estateagent.co.uk">info@estateagent.co.uk</a>
            </li>
            <li>
              <FaMapMarkerAlt className="footer-icon" />
              <a href="https://www.google.com/maps/search/?api=1&query=London,+United+Kingdom" 
                target="_blank" 
                rel="noopener noreferrer">
                London, United Kingdom
              </a>
            </li>
          </ul>
        </div>

        {/* Follow Us */}
        <div className="footer-col">
          <h4>Follow Us</h4>
          <div className="social-icons">
            <a href="#" aria-label="Instagram"><FaInstagram /></a>
            <a href="#" aria-label="Twitter"><FaTwitter /></a>
            <a href="#" aria-label="Facebook"><FaFacebook /></a>
            <a href="#" aria-label="LinkedIn"><FaLinkedin /></a>
          </div>
        </div>
      </div>

      {/* Copyright & Legal */}
      <div className="footer-bottom container">
        <p>© {currentYear} Estate Agent. All rights reserved.</p>
        <div className="footer-legal">
          <a href="#">Privacy Policy</a>
          <span className="separator">|</span>
          <a href="#">Terms of Service</a>
          <span className="separator">|</span>
          <a href="#">Cookie Policy</a>
        </div>
      </div>
    </footer>
  );
};

export default Footer;