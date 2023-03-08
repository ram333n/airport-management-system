function unlinkFlightFromCrewMember(passNumber, flightNumber) {
  const answer = confirm('Do you want to unlink flight from crew member?')

  if (!answer) {
    return
  }

  $.ajax({
    url: '/crew/' + passNumber + '/flights/' + flightNumber,
    type: 'DELETE',
    success: (data) => {
      window.location.href = '/crew/' + passNumber
    }
  })
}