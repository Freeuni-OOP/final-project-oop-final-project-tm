import './Service.css'
import 'react'
import 'react-router-dom'
import ProfilePic from '../../assets/tempProfilePicture.jpeg';
import {useNavigate} from "react-router-dom";

function Service() {
    const navigate = useNavigate();
    return(
        <div className="service-service">
            <header className="service-service-header">
               <div className="service-profile-section">
                   <img
                        src={ProfilePic}
                        alt="profile"
                        className="service-profile-picture"
                        onClick={() => window.location.href = '/user-profile'}
                   />
               </div>
                <div className="service-fields-section">
                    <div className='service.description'>
                        <label htmlFor='description' className='service-title-label'>
                            Description
                        </label>
                        <textarea className='service-description-text-area' placeholder='Nothing for now :p'/>
                    </div>
                </div>
            </header>
            <div className='home-return-class'>
                <button
                    onClick={() => navigate('/')}
                    className='service-return-home-page-button'>
                    home
                </button>
            </div>
        </div>
    );
}

export default Service;