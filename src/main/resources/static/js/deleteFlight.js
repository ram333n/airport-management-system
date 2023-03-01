function deleteFlight(flightNumber) {
  const answer = confirm("Do you want to delete flight?")

  if (!answer) {
    return
  }

  $.ajax({
    url: '/flights/' + flightNumber,
    type: 'DELETE',
    success: (data) => {
      window.location.href = '/flights'
    }
  })
}