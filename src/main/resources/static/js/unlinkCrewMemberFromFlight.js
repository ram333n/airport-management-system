function unlinkCrewMemberFromFlight(flightNumber, passNumber) {
  const answer = confirm('Do you want to unlink crew member from flight?')

  if (!answer) {
    return
  }

  $.ajax({
    url: '/flights/' + flightNumber + '/crew/' + passNumber,
    type: 'DELETE',
    success: (data) => {
      window.location.href = '/flights/' + flightNumber
    }
  })
}