const UserProfile = ({name, age, gender, imageNumber}) => {

    gender = gender ==="MALE" ? "men" : "women"

    return (<div>
            <h1>{name}</h1>
            <p>{age}</p>
            <img src={`https://randomuser.me/api/portraits/${gender}/${imageNumber}.jpg`} alt="image"/>
             </div>
    )
}
export default UserProfile;