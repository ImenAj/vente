(function() {
    'use strict';

    angular
        .module('pfeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('avoirs', {
            parent: 'entity',
            url: '/avoirs',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.avoirs.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/avoirs/avoirs.html',
                    controller: 'AvoirsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('avoirs');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('avoirs-detail', {
            parent: 'avoirs',
            url: '/avoirs/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.avoirs.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/avoirs/avoirs-detail.html',
                    controller: 'AvoirsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('avoirs');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Avoirs', function($stateParams, Avoirs) {
                    return Avoirs.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'avoirs',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('avoirs-detail.edit', {
            parent: 'avoirs-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/avoirs/avoirs-dialog.html',
                    controller: 'AvoirsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Avoirs', function(Avoirs) {
                            return Avoirs.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('avoirs.new', {
            parent: 'avoirs',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/avoirs/avoirs-dialog.html',
                    controller: 'AvoirsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                numero_factureAvoirs: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('avoirs', null, { reload: 'avoirs' });
                }, function() {
                    $state.go('avoirs');
                });
            }]
        })
        .state('avoirs.edit', {
            parent: 'avoirs',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/avoirs/avoirs-dialog.html',
                    controller: 'AvoirsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Avoirs', function(Avoirs) {
                            return Avoirs.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('avoirs', null, { reload: 'avoirs' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('avoirs.delete', {
            parent: 'avoirs',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/avoirs/avoirs-delete-dialog.html',
                    controller: 'AvoirsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Avoirs', function(Avoirs) {
                            return Avoirs.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('avoirs', null, { reload: 'avoirs' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
